import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class App {
    // Frame principale dell'applicazione che mostra i pulsanti per scegliere il
    // tipo di utente (Studente/Docente/Segreteria)
    private static JFrame mainFrame;
    // Frame che gestisce il login e le funzionalità dello studente
    private static JFrame studenteFrame;
    // Frame che gestisce il login e le funzionalità del docente
    private static JFrame docenteFrame;
    // Frame che gestisce il login e le funzionalità della segreteria
    private static JFrame segreteriaFrame;
    private static DefaultTableModel studentiTableModel;
    private static JTable studentiTable;

    // Variabili per tenere traccia degli utenti correnti
    private static Utente studenteCorrente = null;
    private static Utente docenteCorrente = null;
    private static Utente segreteriaCorrente = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creo la finestra principale
        mainFrame = new JFrame("Segreteria studenti");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 500);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        // Pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel titleLabel = new JLabel("Segreteria studenti", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello sinistro per il pulsante Segreteria
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        JButton segreteriaButton = new JButton("Segreteria");
        segreteriaButton.setFont(new Font("Arial", Font.PLAIN, 14));
        segreteriaButton.setPreferredSize(new Dimension(150, 40));
        segreteriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSegreteriaFrame();
            }
        });
        leftPanel.add(segreteriaButton);
        buttonPanel.add(leftPanel, BorderLayout.WEST);

        // Linea divisoria verticale
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        buttonPanel.add(separator, BorderLayout.CENTER);

        // Pannello destro per i pulsanti Docente e Studente
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);

        JButton docenteButton = new JButton("Docente");
        docenteButton.setFont(new Font("Arial", Font.PLAIN, 14));
        docenteButton.setPreferredSize(new Dimension(150, 40));
        docenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDocenteFrame();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(docenteButton, gbc);

        JButton studenteButton = new JButton("Studente");
        studenteButton.setFont(new Font("Arial", Font.PLAIN, 14));
        studenteButton.setPreferredSize(new Dimension(150, 40));
        studenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStudenteFrame();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        rightPanel.add(studenteButton, gbc);

        buttonPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Crediti
        JLabel creditsLabel = new JLabel(
                "Realizzato da: Guadagnuolo Alessandro 0124001570 | Domenico Merola 0124001705", SwingConstants.CENTER);
        creditsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        creditsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(creditsLabel, BorderLayout.SOUTH);

        // Aggiungo il pannello alla finestra
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    // Metodo per aggiornare la lista degli studenti
    private static void refreshStudentiList() {
        studentiTableModel.setRowCount(0);
        Object[][] studenti = Segreteria.getAllStudenti();
        for (Object[] studente : studenti) {
            Object[] row = {
                    studente[0], // matricola
                    studente[1], // nome
                    studente[2], // cognome
                    studente[3], // data_nascita
                    studente[4], // residenza
                    ((Boolean) studente[5]) ? "pagate" : "da pagare" // tasse
            };
            studentiTableModel.addRow(row);
        }
    }

    // Metodo per mostrare la gestione studenti
    private static void showGestioneStudenti() {
        JFrame gestioneFrame = new JFrame("Gestione Studenti");
        gestioneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gestioneFrame.setSize(800, 600);
        gestioneFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello superiore per i pulsanti
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Pulsante per aggiungere uno studente
        JButton addButton = new JButton("Inserisci un nuovo studente");
        addButton.addActionListener(e -> mostraDialogAggiungiStudente());
        topPanel.add(addButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabella degli studenti
        studentiTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentiTableModel.addColumn("Matricola");
        studentiTableModel.addColumn("Nome");
        studentiTableModel.addColumn("Cognome");
        studentiTableModel.addColumn("Data di Nascita");
        studentiTableModel.addColumn("Residenza");
        studentiTableModel.addColumn("Tasse");

        studentiTable = new JTable(studentiTableModel);
        JScrollPane scrollPane = new JScrollPane(studentiTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        gestioneFrame.add(mainPanel);
        gestioneFrame.setVisible(true);

        // Aggiorna la lista degli studenti
        refreshStudentiList();
    }

    private static void mostraDialogAggiungiStudente() {
        JDialog dialog = new JDialog(segreteriaFrame, "Aggiungi Studente", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campi del form
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Matricola:"), gbc);
        gbc.gridx = 1;
        JTextField matricolaField = new JTextField(20);
        formPanel.add(matricolaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        JTextField nomeField = new JTextField(20);
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1;
        JTextField cognomeField = new JTextField(20);
        formPanel.add(cognomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Data di Nascita (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        JTextField dataNascitaField = new JTextField(20);
        formPanel.add(dataNascitaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Residenza:"), gbc);
        gbc.gridx = 1;
        JTextField residenzaField = new JTextField(20);
        formPanel.add(residenzaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Tasse pagate:"), gbc);
        gbc.gridx = 1;
        JCheckBox tasseCheckbox = new JCheckBox();
        formPanel.add(tasseCheckbox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Aggiungi");
        addButton.addActionListener(e -> {
            // Validazione dei campi
            String matricola = matricolaField.getText().trim();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String dataNascita = dataNascitaField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String residenza = residenzaField.getText().trim();
            boolean tassePagate = tasseCheckbox.isSelected();

            if (matricola.isEmpty() || nome.isEmpty() || cognome.isEmpty() ||
                    dataNascita.isEmpty() || password.isEmpty() || residenza.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Tutti i campi sono obbligatori",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Segreteria.addStudente(matricola, nome, cognome, dataNascita, password, residenza, tassePagate)) {
                dialog.dispose();
                refreshStudentiList();
                JOptionPane.showMessageDialog(null,
                        "Studente aggiunto con successo",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Errore durante l'inserimento dello studente",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Metodi per il login
    private static boolean loginStudente(String matricola, String password) {
        LoginUtente loginStrategy = new LoginStudente();
        return loginStrategy.login(matricola, password);
    }

    private static boolean loginDocente(String id, String password) {
        LoginUtente loginStrategy = new LoginDocente();
        return loginStrategy.login(id, password);
    }

    private static boolean loginSegreteria(String id, String password) {
        LoginUtente loginStrategy = new LoginSegreteria();
        return loginStrategy.login(id, password);
    }

    // Metodi per aprire le finestre
    private static void openStudenteFrame() {
        if (studenteFrame == null) {
            studenteFrame = new JFrame("Login Studente");
            studenteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            studenteFrame.setSize(400, 300);
            studenteFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Titolo
            JLabel titleLabel = new JLabel("Area Studenti", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(titleLabel, BorderLayout.NORTH);

            // Pannello per il login
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campo matricola
            gbc.gridx = 0;
            gbc.gridy = 0;
            loginPanel.add(new JLabel("Matricola:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            JTextField matricolaField = new JTextField(15);
            loginPanel.add(matricolaField, gbc);

            // Campo password
            gbc.gridx = 0;
            gbc.gridy = 1;
            loginPanel.add(new JLabel("Password:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            JPasswordField passwordField = new JPasswordField(15);
            loginPanel.add(passwordField, gbc);

            // Pulsante login
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String matricola = matricolaField.getText();
                    String password = new String(passwordField.getPassword());

                    if (loginStudente(matricola, password)) {
                        // Login riuscito, mostra le funzionalità dello studente
                        showStudenteFunctionality(matricola);
                    }
                }
            });
            loginPanel.add(loginButton, gbc);

            panel.add(loginPanel, BorderLayout.CENTER);
            studenteFrame.add(panel);
        }
        studenteFrame.setVisible(true);
    }

    private static void openDocenteFrame() {
        if (docenteFrame == null) {
            docenteFrame = new JFrame("Login Docente");
            docenteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            docenteFrame.setSize(400, 300);
            docenteFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Titolo
            JLabel titleLabel = new JLabel("Area Docenti", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(titleLabel, BorderLayout.NORTH);

            // Pannello per il login
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campo ID
            gbc.gridx = 0;
            gbc.gridy = 0;
            loginPanel.add(new JLabel("ID Docente:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            JTextField idField = new JTextField(15);
            loginPanel.add(idField, gbc);

            // Campo password
            gbc.gridx = 0;
            gbc.gridy = 1;
            loginPanel.add(new JLabel("Password:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            JPasswordField passwordField = new JPasswordField(15);
            loginPanel.add(passwordField, gbc);

            // Pulsante login
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = idField.getText();
                    String password = new String(passwordField.getPassword());

                    if (loginDocente(id, password)) {
                        // Login riuscito, mostra le funzionalità del docente
                        showDocenteFunctionality(id);
                    }
                }
            });
            loginPanel.add(loginButton, gbc);

            panel.add(loginPanel, BorderLayout.CENTER);
            docenteFrame.add(panel);
        }
        docenteFrame.setVisible(true);
    }

    private static void openSegreteriaFrame() {
        if (segreteriaFrame == null) {
            segreteriaFrame = new JFrame("Login Segreteria");
            segreteriaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            segreteriaFrame.setSize(400, 300);
            segreteriaFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Titolo
            JLabel titleLabel = new JLabel("Area Segreteria", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(titleLabel, BorderLayout.NORTH);

            // Pannello per il login
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campo ID
            gbc.gridx = 0;
            gbc.gridy = 0;
            loginPanel.add(new JLabel("ID Segreteria:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            JTextField idField = new JTextField(15);
            loginPanel.add(idField, gbc);

            // Campo password
            gbc.gridx = 0;
            gbc.gridy = 1;
            loginPanel.add(new JLabel("Password:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            JPasswordField passwordField = new JPasswordField(15);
            loginPanel.add(passwordField, gbc);

            // Pulsante login
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = idField.getText();
                    String password = new String(passwordField.getPassword());

                    if (loginSegreteria(id, password)) {
                        // Login riuscito, mostra le funzionalità della segreteria
                        showSegreteriaFunctionality(id);
                    }
                }
            });
            loginPanel.add(loginButton, gbc);

            panel.add(loginPanel, BorderLayout.CENTER);
            segreteriaFrame.add(panel);
        }
        segreteriaFrame.setVisible(true);
    }

    // Metodi per mostrare le funzionalità dopo il login
    private static void showStudenteFunctionality(String matricola) {
        // Crea l'istanza dello studente usando il Factory
        studenteCorrente = ConcreteUtenteFactory.getUtente("studente", matricola, "", "", "");

        // Rimuovo il pannello di login
        studenteFrame.getContentPane().removeAll();

        // Creo un nuovo pannello per le funzionalità
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel titleLabel = new JLabel("Funzionalità Studente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Pannello per i pulsanti delle funzionalità
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Pulsante Prenotazione
        JButton prenotazioneButton = new JButton("Prenota Esame");
        prenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementazione della prenotazione
                if (studenteCorrente instanceof Studente) {
                    ((Studente) studenteCorrente).effettuaPrenotazione(matricola);
                } else {
                    JOptionPane.showMessageDialog(studenteFrame,
                            "Errore: utente non riconosciuto come studente",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(prenotazioneButton);

        // Pulsante Gestione Notifiche
        JButton notificheButton = new JButton("Notifiche");
        notificheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementazione della gestione notifiche
                    if (studenteCorrente instanceof Studente) {
                        ((Studente) studenteCorrente).valutaVoto(matricola);
                    } else {
                        JOptionPane.showMessageDialog(studenteFrame,
                                "Errore: utente non riconosciuto come studente",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    }
            }
        });
        buttonPanel.add(notificheButton);

        // Pulsante Effettua Test
        JButton testButton = new JButton("Effettua Test");
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementazione del test
                JOptionPane.showMessageDialog(studenteFrame,
                        "Funzionalità di test in sviluppo",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(testButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Modifica il pulsante logout
        JButton logoutButton = new JButton("Disconnetti");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effettuaLogout("studente", studenteFrame);
            }
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        panel.add(logoutPanel, BorderLayout.SOUTH);

        studenteFrame.add(panel);
        studenteFrame.revalidate();
        studenteFrame.repaint();
    }

    private static void showDocenteFunctionality(String id) {
        // Crea l'istanza del docente usando il Factory
        docenteCorrente = ConcreteUtenteFactory.getUtente("docente", id, "", "", "");
        DocenteSubject docenteSubject = new DocenteSubject();
        // Rimuovo il pannello di login
        docenteFrame.getContentPane().removeAll();

        // Creo un nuovo pannello per le funzionalità
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel titleLabel = new JLabel("Funzionalità Docente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Pannello per i pulsanti delle funzionalità
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // Pulsante Inserisci Appello
        JButton appelloButton = new JButton("Inserisci Appello");
        appelloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chiamata al metodo inserisci_appello della classe Docente
                if (docenteCorrente instanceof Docente) {
                    ((Docente) docenteCorrente).inserisci_appello();
                } else {
                    JOptionPane.showMessageDialog(docenteFrame,
                            "Errore: utente non riconosciuto come docente",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(appelloButton);

        // Pulsante Inserisci Voto
        JButton votoButton = new JButton("Inserisci Voto");

        votoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (docenteCorrente instanceof Docente) {
                    ((Docente) docenteCorrente).inserisci_voto(docenteSubject);
                } else {
                    JOptionPane.showMessageDialog(docenteFrame,
                            "Errore: utente non riconosciuto come docente",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(votoButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Modifica il pulsante logout
        JButton logoutButton = new JButton("Disconnetti");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effettuaLogout("docente", docenteFrame);
            }
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        panel.add(logoutPanel, BorderLayout.SOUTH);

        docenteFrame.add(panel);
        docenteFrame.revalidate();
        docenteFrame.repaint();
    }

    private static void showSegreteriaFunctionality(String id) {
        // Crea l'istanza della segreteria usando il Factory
        segreteriaCorrente = ConcreteUtenteFactory.getUtente("segreteria", id, "", "", "");

        // Rimuovo il pannello di login
        segreteriaFrame.getContentPane().removeAll();

        // Creo un nuovo pannello per le funzionalità
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel titleLabel = new JLabel("Funzionalità Segreteria", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Pannello per i pulsanti delle funzionalità
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Pulsante Gestione Studenti
        JButton studentiButton = new JButton("Gestione Studenti");
        studentiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGestioneStudenti();
            }
        });
        buttonPanel.add(studentiButton);

        // Pulsante Visualizza Informazioni Studente
        JButton infoStudenteButton = new JButton("Visualizza informazioni studente");
        infoStudenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricola = JOptionPane.showInputDialog(segreteriaFrame,
                        "Inserisci la matricola dello studente:");
                if (matricola != null && !matricola.trim().isEmpty()) {
                    String result = Segreteria.visualizza_informazioni(matricola);
                    JTextArea textArea = new JTextArea(result);
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
                    JOptionPane.showMessageDialog(segreteriaFrame, scrollPane, "Informazioni Studente",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(infoStudenteButton);

        // Pulsante Visualizza Esiti Test per Corso
        JButton esitiTestButton = new JButton("Visualizza gli esiti dei test per singolo corso");
        esitiTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCorso = JOptionPane.showInputDialog(segreteriaFrame, "Inserisci il nome del corso:");
                if (nomeCorso != null && !nomeCorso.trim().isEmpty()) {
                    String result = Segreteria.visualizza_esiti(nomeCorso);
                    JTextArea textArea = new JTextArea(result);
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
                    JOptionPane.showMessageDialog(segreteriaFrame, scrollPane, "Esiti Esame",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(esitiTestButton);

        // Pulsante Visualizza Esiti per Corso di Laurea
        JButton esitiCorsoButton = new JButton("Visualizza gli esiti per un intero Corso di Laurea");
        esitiCorsoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCorso = JOptionPane.showInputDialog(segreteriaFrame,
                        "Inserisci il nome del corso di laurea:");
                if (nomeCorso != null && !nomeCorso.trim().isEmpty()) {
                    String result = Segreteria.visualizza_esiti_corso(nomeCorso);
                    JTextArea textArea = new JTextArea(result);
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
                    JOptionPane.showMessageDialog(segreteriaFrame, scrollPane, "Esiti Corso di Laurea",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(esitiCorsoButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Modifica il pulsante logout
        JButton logoutButton = new JButton("Disconnetti");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effettuaLogout("segreteria", segreteriaFrame);
            }
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        panel.add(logoutPanel, BorderLayout.SOUTH);

        segreteriaFrame.add(panel);
        segreteriaFrame.revalidate();
        segreteriaFrame.repaint();
    }

    private static void effettuaLogout(String tipoUtente, JFrame frame) {
        // Chiude la sessione utilizzando la classe di login appropriata
        LoginUtente loginStrategy = null;
        switch (tipoUtente.toLowerCase()) {
            case "studente":
                loginStrategy = new LoginStudente();
                studenteCorrente = null;
                frame.dispose();
                studenteFrame = null;
                openStudenteFrame();
                break;
            case "docente":
                loginStrategy = new LoginDocente();
                docenteCorrente = null;
                frame.dispose();
                docenteFrame = null;
                openDocenteFrame();
                break;
            case "segreteria":
                loginStrategy = new LoginSegreteria();
                segreteriaCorrente = null;
                frame.dispose();
                segreteriaFrame = null;
                openSegreteriaFrame();
                break;
        }
        if (loginStrategy != null) {
            loginStrategy.logout();
        }
    }
}
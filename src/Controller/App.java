package Controller;

import Factory.ConcreteUtenteFactory;
import Model.Docente;
import Model.Segreteria;
import Model.Studente;
import Model.Utente;
import Observer.DocenteSubject;
import Strategy.LoginDocente;
import Strategy.LoginSegreteria;
import Strategy.LoginStudente;
import Strategy.LoginUtente;
import View.DocentePanel;
import View.SegreteriaPanel;
import View.StudentePanel;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class App {
    // Frame principale dell'applicazione che mostra i pulsanti per scegliere il
    // tipo di utente (Model.Studente/Model.Docente/Model.Segreteria)
    private static JFrame mainFrame;

    // Frame che gestisce il login e le funzionalità dello studente
    public static JFrame studenteFrame;

    // Frame che gestisce il login e le funzionalità del docente
    public static JFrame docenteFrame;

    // Frame che gestisce il login e le funzionalità della segreteria
    public static JFrame segreteriaFrame;

    private static DefaultTableModel studentiTableModel;
    private static JTable studentiTable;
    private static SegreteriaPanel segreteriaPanel;
    private static DocentePanel docentePanel;
    private static StudentePanel studentePanel;

    // Variabili per tenere traccia degli utenti correnti
    private static Utente studenteCorrente = null;
    private static Utente docenteCorrente = null;
    private static Utente segreteriaCorrente = null;

    public static void main(String[] args) {
        //Questo blocco serve per rendere l'interfaccia grafica simile a quella del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creo il frame principale
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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // View Segreteria
        segreteriaPanel = new SegreteriaPanel();
        buttonPanel.add(segreteriaPanel, BorderLayout.WEST);

        // Linea divisoria verticale
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 100));
        buttonPanel.add(separator, BorderLayout.CENTER);

        // Pannello destro per i pulsanti Model.Docente e Model.Studente
        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 80, 0));
        docentePanel = new DocentePanel();
        studentePanel = new StudentePanel();
        rightPanel.add(docentePanel);
        rightPanel.add(studentePanel);

        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
        buttonPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Crediti
        JLabel creditsLabel = new JLabel(
                "Realizzato da: Guadagnuolo Alessandro 0124001570 | Merola Domenico 0124001705", SwingConstants.CENTER);
        creditsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        creditsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(creditsLabel, BorderLayout.SOUTH);

        for (ActionListener al : segreteriaPanel.getSegreteriaButton().getActionListeners()) {
            segreteriaPanel.getSegreteriaButton().removeActionListener(al);
        }
        segreteriaPanel.getSegreteriaButton().addActionListener(e -> openSegreteriaFrame());
        for (ActionListener al : docentePanel.getDocenteButton().getActionListeners()) {
            docentePanel.getDocenteButton().removeActionListener(al);
        }
        docentePanel.getDocenteButton().addActionListener(e -> openDocenteFrame());
        for (ActionListener al : studentePanel.getStudenteButton().getActionListeners()) {
            docentePanel.getDocenteButton().removeActionListener(al);
        }
        studentePanel.getStudenteButton().addActionListener(e -> openStudenteFrame());

        // Aggiungo il pannello alla finestra
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setVisible(true);

    }

    // Metodo per aggiornare la lista degli studenti - Segreteria
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
                    ((Boolean) studente[5]) ? "pagate" : "da pagare", // tasse
                    studente[6] // corso di laurea
            };
            studentiTableModel.addRow(row);
        }
    }

    // Metodo per mostrare la gestione studenti - Segreteria
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
        for (ActionListener al : addButton.getActionListeners()) {
            addButton.removeActionListener(al);
        }
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
        studentiTableModel.addColumn("Corso di Laurea");

        studentiTable = new JTable(studentiTableModel);
        JScrollPane scrollPane = new JScrollPane(studentiTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        gestioneFrame.add(mainPanel);
        gestioneFrame.setVisible(true);

        // Aggiorna la lista degli studenti
        refreshStudentiList();
    }

    // Metodo per aggiungere un nuovo studente
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

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Corso di laurea:"), gbc);
        gbc.gridx = 1;
        JTextField corsoDiLaureaField = new JTextField(20);
        formPanel.add(corsoDiLaureaField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Aggiungi");
        for (ActionListener al : addButton.getActionListeners()) {
            addButton.removeActionListener(al);
        }
        addButton.addActionListener(e -> {
            // Validazione dei campi
            String matricola = matricolaField.getText().trim();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String dataNascita = dataNascitaField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String residenza = residenzaField.getText().trim();
            boolean tassePagate = tasseCheckbox.isSelected();
            String corsoDiLaurea = corsoDiLaureaField.getText().trim();

            if (matricola.isEmpty() || nome.isEmpty() || cognome.isEmpty() ||
                    dataNascita.isEmpty() || password.isEmpty() || residenza.isEmpty() || corsoDiLaurea.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Tutti i campi sono obbligatori",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Segreteria.addStudente(matricola, nome, cognome, dataNascita, password, residenza, tassePagate, corsoDiLaurea)) {
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
        for (ActionListener al : cancelButton.getActionListeners()) {
            cancelButton.removeActionListener(al);
        }
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
        studentePanel.showStudenteFrame();
        for (ActionListener al : studentePanel.getLoginButton().getActionListeners()) {
            studentePanel.getLoginButton().removeActionListener(al);
        }
        studentePanel.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricola = studentePanel.getMatricolaField().getText();
                String password = new String(studentePanel.getPasswordField().getPassword());

                if (loginStudente(matricola, password)) {
                    // Login riuscito, mostra le funzionalità dello studente
                    showStudenteFunctionality(matricola);
                }
            }
        });
        studenteFrame.setVisible(true);
    }

    private static void openDocenteFrame() {
        docentePanel.showDocenteFrame();
        for (ActionListener al : docentePanel.getLoginButton().getActionListeners()) {
            docentePanel.getLoginButton().removeActionListener(al);
        }
        docentePanel.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = docentePanel.getIdField().getText();
                String password = new String(docentePanel.getPasswordField().getPassword());

                if (loginDocente(id, password)) {
                    // Login riuscito, mostra le funzionalità del docente
                    showDocenteFunctionality(id);
                }
            }
        });
        docenteFrame.setVisible(true);
    }

    private static void openSegreteriaFrame() {
        segreteriaPanel.showSegreteriaFrame();
        for (ActionListener al : segreteriaPanel.getLoginButton().getActionListeners()) {
            segreteriaPanel.getLoginButton().removeActionListener(al);
        }
        segreteriaPanel.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = segreteriaPanel.getIdField().getText();
                String password = new String(segreteriaPanel.getPasswordField().getPassword());

                if (loginSegreteria(id, password)) {
                    // Login riuscito, mostra le funzionalità della segreteria
                    showSegreteriaFunctionality(id);
                }
            }
        });
        segreteriaFrame.setVisible(true);
    }

    // Metodi per mostrare le funzionalità dopo il login
    private static void showStudenteFunctionality(String matricola) {
        studentePanel.getPanel().removeAll();
        studentePanel.getButtonPanel().removeAll();

        // Crea l'istanza dello studente usando il Factory
        studenteCorrente = ConcreteUtenteFactory.getUtente("studente", matricola, "", "", "");

        // Rimuovo il pannello di login
        studenteFrame.getContentPane().removeAll();

        // Layout principale
        studentePanel.getPanel().setLayout(new BorderLayout());
        studentePanel.getPanel().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        studentePanel.getTitleLabel().setFont(new Font("Arial", Font.BOLD, 18));
        studentePanel.getPanel().add(studentePanel.getTitleLabel(), BorderLayout.NORTH);

        // Pannello centrale con i pulsanti principali
        studentePanel.getButtonPanel().setLayout(new GridLayout(3, 1, 10, 10));

        // Prenota Esame
        for (ActionListener al : studentePanel.getPrenotazioneButton().getActionListeners()) {
            studentePanel.getPrenotazioneButton().removeActionListener(al);
        }
        studentePanel.getPrenotazioneButton().addActionListener(e -> {
            if (studenteCorrente instanceof Studente) {
                ((Studente) studenteCorrente).effettuaPrenotazione(matricola);
            } else {
                JOptionPane.showMessageDialog(studenteFrame,
                        "Errore: utente non riconosciuto come studente",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        studentePanel.getButtonPanel().add(studentePanel.getPrenotazioneButton());

        // Effettua Test
        for (ActionListener al : studentePanel.getTestButton().getActionListeners()) {
            studentePanel.getTestButton().removeActionListener(al);
        }
        studentePanel.getTestButton().addActionListener(e -> {
            if (studenteCorrente instanceof Studente) {
                ((Studente) studenteCorrente).effettuaTest();
            } else {
                JOptionPane.showMessageDialog(studenteFrame,
                        "Errore: utente non riconosciuto come studente",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        studentePanel.getButtonPanel().add(studentePanel.getTestButton());

        // Aggiunge il pannello con i pulsanti principali al centro
        studentePanel.getPanel().add(studentePanel.getButtonPanel(), BorderLayout.CENTER);

        // Pannello inferiore con Notifiche (a sinistra) e Disconnetti (a destra)
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Pulsante Notifiche
        for (ActionListener al : studentePanel.getNotificheButton().getActionListeners()) {
            studentePanel.getNotificheButton().removeActionListener(al);
        }
        studentePanel.getNotificheButton().addActionListener(e -> {
            if (studenteCorrente instanceof Studente) {
                ((Studente) studenteCorrente).valutaVoto(matricola);
            } else {
                JOptionPane.showMessageDialog(studenteFrame,
                        "Errore: utente non riconosciuto come studente",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomPanel.add(studentePanel.getNotificheButton(), BorderLayout.WEST);

        // Pulsante Logout (Disconnetti)
        studentePanel.getLogoutButton().setFont(new Font("Arial", Font.PLAIN, 14));
        for (ActionListener al : studentePanel.getLogoutButton().getActionListeners()) {
            studentePanel.getLogoutButton().removeActionListener(al);
        }
        for (ActionListener al : studentePanel.getLogoutButton().getActionListeners()) {
            studentePanel.getLogoutButton().removeActionListener(al);
        }
        studentePanel.getLogoutButton().addActionListener(e -> effettuaLogout("studente", studenteFrame));
        bottomPanel.add(studentePanel.getLogoutButton(), BorderLayout.EAST);

        // Aggiunge il pannello inferiore al pannello principale
        studentePanel.getPanel().add(bottomPanel, BorderLayout.SOUTH);

        // Mostra il tutto nel frame
        studenteFrame.add(studentePanel.getPanel());
        studenteFrame.revalidate();
        studenteFrame.repaint();
    }

    private static void showDocenteFunctionality(String id) {
        docentePanel.getPanel().removeAll();
        docentePanel.getButtonPanel().removeAll();
        // Crea l'istanza del docente usando il Factory
        docenteCorrente = ConcreteUtenteFactory.getUtente("docente", id, "", "", "");
        DocenteSubject docenteSubject = new DocenteSubject();
        // Rimuovo il pannello di login
        docenteFrame.getContentPane().removeAll();

        // Creo un nuovo pannello per le funzionalità
        docentePanel.getPanel().setLayout(new BorderLayout());
        docentePanel.getPanel().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        docentePanel.getTitleLabel().setFont(new Font("Arial", Font.BOLD, 18));
        docentePanel.getPanel().add(docentePanel.getTitleLabel(), BorderLayout.NORTH);

        // Pannello per i pulsanti delle funzionalità
        docentePanel.getButtonPanel().setLayout(new GridLayout(2, 1, 10, 10));

        // Pulizia actionListner
        for (ActionListener al : docentePanel.getAppelloButton().getActionListeners()) {
            docentePanel.getAppelloButton().removeActionListener(al);
        }

        // Pulsante Inserisci Appello
        for (ActionListener al : docentePanel.getAppelloButton().getActionListeners()) {
            docentePanel.getAppelloButton().removeActionListener(al);
        }
        docentePanel.getAppelloButton().addActionListener(new ActionListener() {
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
        docentePanel.getButtonPanel().add(docentePanel.getAppelloButton());
        for (ActionListener al : docentePanel.getVotoButton().getActionListeners()) {
            docentePanel.getVotoButton().removeActionListener(al);
        }
        // Pulsante Inserisci Voto
        docentePanel.getVotoButton().addActionListener(new ActionListener() {
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

        docentePanel.getButtonPanel().add(docentePanel.getVotoButton());
        docentePanel.getPanel().add(docentePanel.getButtonPanel(), BorderLayout.CENTER);

        // Modifica il pulsante logout
        docentePanel.getLogoutButton().setFont(new Font("Arial", Font.PLAIN, 14));
        for (ActionListener al : docentePanel.getLogoutButton().getActionListeners()) {
            docentePanel.getLogoutButton().removeActionListener(al);
        }
        for (ActionListener al : docentePanel.getLogoutButton().getActionListeners()) {
            docentePanel.getLogoutButton().removeActionListener(al);
        }
        docentePanel.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effettuaLogout("docente", docenteFrame);
            }
        });

        docentePanel.getLogoutPanel().add(docentePanel.getLogoutButton());
        docentePanel.getPanel().add(docentePanel.getLogoutPanel(), BorderLayout.SOUTH);

        docenteFrame.add(docentePanel.getPanel());
        docenteFrame.revalidate();
        docenteFrame.repaint();
    }

    private static void showSegreteriaFunctionality(String id) {
        segreteriaPanel.getPanel().removeAll();
        segreteriaPanel.getButtonPanel().removeAll();

        // Crea l'istanza della segreteria usando il Factory
        segreteriaCorrente = ConcreteUtenteFactory.getUtente("segreteria", id, "", "", "");

        // Rimuovo il pannello di login
        segreteriaFrame.getContentPane().removeAll();

        segreteriaPanel.showGestioneStudentiView();
        for (ActionListener al : segreteriaPanel.getStudentiButton().getActionListeners()) {
            segreteriaPanel.getStudentiButton().removeActionListener(al);
        }
        segreteriaPanel.getStudentiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGestioneStudenti();
            }
        });
        segreteriaPanel.getButtonPanel().add(segreteriaPanel.getStudentiButton());

        // Pulsante Visualizza Informazioni Studente
        for (ActionListener al : segreteriaPanel.getInfoStudenteButton().getActionListeners()) {
            segreteriaPanel.getInfoStudenteButton().removeActionListener(al);
        }
        segreteriaPanel.getInfoStudenteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricola = JOptionPane.showInputDialog(segreteriaFrame,
                        "Inserisci la matricola dello studente:");
                if (matricola != null && !matricola.trim().isEmpty()) {
                    String result = Segreteria.visualizza_informazioni(matricola);
                    segreteriaPanel.mostraInfo(result, "Informazioni Studente");
                }
            }
        });
        segreteriaPanel.getButtonPanel().add(segreteriaPanel.getInfoStudenteButton());

        // Pulsante Visualizza Esiti Test per Corso
        for (ActionListener al : segreteriaPanel.getEsitiTestButton().getActionListeners()) {
            segreteriaPanel.getEsitiTestButton().removeActionListener(al);
        }
        segreteriaPanel.getEsitiTestButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCorso = JOptionPane.showInputDialog(segreteriaFrame, "Inserisci il nome del corso:");
                if (nomeCorso != null && !nomeCorso.trim().isEmpty()) {
                    String result = Segreteria.visualizza_esiti(nomeCorso);
                    segreteriaPanel.mostraEsitiEsame(result,"Esiti esame");
                }
            }
        });
        segreteriaPanel.getButtonPanel().add(segreteriaPanel.getEsitiTestButton());

        // Pulsante Visualizza Esiti per Corso di Laurea
        for (ActionListener al : segreteriaPanel.getEsitiCorsoButton().getActionListeners()) {
            segreteriaPanel.getEsitiCorsoButton().removeActionListener(al);
        }
        segreteriaPanel.getEsitiCorsoButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCorso = JOptionPane.showInputDialog(segreteriaFrame,
                        "Inserisci il nome del corso di laurea:");
                if (nomeCorso != null && !nomeCorso.trim().isEmpty()) {
                    String result = Segreteria.visualizza_esiti_corso(nomeCorso);
                    segreteriaPanel.mostraEsitiCorso(result,"Esiti corso");
                }
            }
        });
        segreteriaPanel.getButtonPanel().add( segreteriaPanel.getEsitiCorsoButton());

        segreteriaPanel.getPanel().add(segreteriaPanel.getButtonPanel(), BorderLayout.CENTER);

        // Modifica il pulsante logout
        segreteriaPanel.getLogoutButton().setFont(new Font("Arial", Font.PLAIN, 14));
        for (ActionListener al : segreteriaPanel.getLogoutButton().getActionListeners()) {
            segreteriaPanel.getLogoutButton().removeActionListener(al);
        }

        for (ActionListener al : segreteriaPanel.getLogoutButton().getActionListeners()) {
            segreteriaPanel.getLogoutButton().removeActionListener(al);
        }
        segreteriaPanel.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effettuaLogout("segreteria", segreteriaFrame);
            }
        });

        segreteriaPanel.getLogoutPanel().add(segreteriaPanel.getLogoutButton());
        segreteriaPanel.getPanel().add(segreteriaPanel.getLogoutPanel(), BorderLayout.SOUTH);

        segreteriaFrame.add(segreteriaPanel.getPanel());
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
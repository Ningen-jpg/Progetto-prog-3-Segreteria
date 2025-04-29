package View;
import javax.swing.*;
import java.awt.*;
import Controller.App;

public class SegreteriaPanel extends JPanel {
    private JButton segreteriaButton = new JButton("Segreteria");
    private JButton studentiButton;
    private JPanel buttonPanel = new JPanel();
    private JPanel panel = new JPanel();
    private JButton infoStudenteButton;
    private JButton esitiTestButton;
    private JButton esitiCorsoButton;
    private JButton logoutButton;
    private JPanel logoutPanel;
    private JPanel loginPanel;
    private JButton loginButton;
    private JTextField idField;
    private JPasswordField passwordField;
    private GridBagConstraints gbc;

    public SegreteriaPanel() {
        setLayout(new BorderLayout(20, 0));
        JPanel panel = new JPanel(new GridBagLayout());
        segreteriaButton.setFont(new Font("Arial",Font.PLAIN,14));
        segreteriaButton.setPreferredSize(new Dimension(130,60));
        panel.add(segreteriaButton);
        add(panel, BorderLayout.WEST);
    }

    public void showSegreteriaFrame(JFrame frame) {
        if (App.segreteriaFrame == null) {
            App.segreteriaFrame = new JFrame("Login Segreteria");
            App.segreteriaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            App.segreteriaFrame.setSize(400, 300);
            App.segreteriaFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Titolo
            JLabel titleLabel = new JLabel("Area Segreteria", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(titleLabel, BorderLayout.NORTH);

            // Pannello per il login
            loginPanel = new JPanel();
            loginPanel.setLayout(new GridBagLayout());
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campo ID
            gbc.gridx = 0;
            gbc.gridy = 0;
            loginPanel.add(new JLabel("ID Segreteria:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            idField = new JTextField(15);
            loginPanel.add(idField, gbc);

            // Campo password
            gbc.gridx = 0;
            gbc.gridy = 1;
            loginPanel.add(new JLabel("Password:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            passwordField = new JPasswordField(15);
            loginPanel.add(passwordField, gbc);

            // Pulsante login
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            loginButton = new JButton("Login");
            loginPanel.add(loginButton, gbc);
            panel.add(loginPanel, BorderLayout.CENTER);
            App.segreteriaFrame.add(panel);
        }
    }

    public void showGestioneStudentiView() {
        // Creo un nuovo pannello per le funzionalità

        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel titleLabel = new JLabel("Funzionalità Segreteria", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Pannello per i pulsanti delle funzionalità

        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Pulsante Gestione Studenti
        studentiButton = new JButton("Gestione Studenti");

        infoStudenteButton = new JButton("Visualizza informazioni studente");

        esitiTestButton = new JButton("Visualizza esiti corso");

        esitiCorsoButton = new JButton("Visualizza esiti corso di Laurea");

        logoutButton = new JButton("Disconnetti");

        logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    }

    public void mostraInfo(String testo, String titolo){

        JTextArea textArea = new JTextArea(testo);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, textArea, titolo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraEsitiEsame(String testo, String titolo){
        JTextArea textArea = new JTextArea(testo);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, titolo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraEsitiCorso(String testo, String titolo){
        JTextArea textArea = new JTextArea(testo);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, titolo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getStudentiButton() { return studentiButton; }

    public JButton getSegreteriaButton() { return segreteriaButton; }

    public JPanel getButtonPanel() { return buttonPanel; }

    public JPanel getPanel() { return panel; }

    public JButton getInfoStudenteButton(){ return infoStudenteButton; }

    public JButton getEsitiTestButton() { return esitiTestButton; }

    public JButton getEsitiCorsoButton(){ return esitiCorsoButton; }

    public JButton getLogoutButton() { return logoutButton; }

    public JPanel getLogoutPanel() { return logoutPanel; }

    public JButton getLoginButton(){ return loginButton; }

    public JTextField getIdField() { return idField; }

    public JPasswordField getPasswordField() { return passwordField; }

    public JPanel getLoginPanel() { return loginPanel; }

    public GridBagConstraints getGbc() { return gbc; }
}


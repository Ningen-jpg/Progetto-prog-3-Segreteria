package View;
import javax.swing.*;
import java.awt.*;

import Controller.App;

public class DocentePanel extends JPanel{
    private JButton docenteButton = new JButton("Docente");
    JPanel panel = new JPanel();
    JLabel titleLabel = new JLabel("Funzionalità Docente", SwingConstants.CENTER);
    JPanel buttonPanel = new JPanel();
    JButton appelloButton = new JButton("Inserisci Appello");
    JButton votoButton = new JButton("Inserisci Voto");
    JButton logoutButton = new JButton("Disconnetti");
    JButton loginButton;
    JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JTextField idField;
    JPasswordField passwordField;

    public DocentePanel() {
        setLayout(new BorderLayout(20, 0));
        JPanel panel = new JPanel(new GridBagLayout());
        docenteButton.setFont(new Font("Arial",Font.PLAIN,14));
        docenteButton.setPreferredSize(new Dimension(130,60));
        panel.add(docenteButton);
        add(panel);
    }

    public JButton getDocenteButton() {
        return docenteButton;
    }

    public void showDocenteFrame(JFrame frame){
        if (App.docenteFrame == null) {
            App.docenteFrame = new JFrame("Login Docente");
            App.docenteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            App.docenteFrame.setSize(400, 300);
            App.docenteFrame.setLocationRelativeTo(null);

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
            idField = new JTextField(15);
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
            loginButton = new JButton("Login");
            loginPanel.add(loginButton, gbc);
            panel.add(loginPanel, BorderLayout.CENTER);
            App.docenteFrame.add(panel);
        }
    }

    public JPanel getPanel(){ return  panel; }
    public JLabel getTitleLabel() { return titleLabel; }
    public JPanel getButtonPanel() { return buttonPanel; }
    public JButton getAppelloButton() { return appelloButton; }
    public JButton getVotoButton() { return votoButton; }
    public JButton getLogoutButton() { return logoutButton; }
    public JPanel getLogoutPanel() { return logoutPanel; }
    public JButton getLoginButton() { return loginButton; }
    public JTextField getIdField() { return idField; }
    public JPasswordField getPasswordField() { return passwordField; }
}

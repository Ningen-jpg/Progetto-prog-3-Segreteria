package View;
import javax.swing.*;
import java.awt.*;
import Controller.App;

public class StudentePanel extends JPanel {
    private JButton studenteButton =  new JButton("Studente");
    JPanel panel = new JPanel();
    JLabel titleLabel = new JLabel("Funzionalità Studente", SwingConstants.CENTER);
    JPanel buttonPanel = new JPanel();
    JButton prenotazioneButton = new JButton("Prenota Esame");
    JButton notificheButton = new JButton("Notifiche");
    JButton testButton = new JButton("Effettua Test");
    JButton logoutButton = new JButton("Disconnetti");
    JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPasswordField passwordField;
    JTextField matricolaField;
    JButton loginButton;

    public StudentePanel() {
        setLayout(new BorderLayout(20, 0));
        JPanel panel = new JPanel(new GridBagLayout());
        studenteButton.setFont(new Font("Arial",Font.PLAIN,14));
        studenteButton.setPreferredSize(new Dimension(170,100));
        panel.add(studenteButton);
        add(panel);
    }

    public void showStudenteFrame(){
        if (App.studenteFrame == null) {
            App.studenteFrame = new JFrame("Login Studente");
            App.studenteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            App.studenteFrame.setSize(400, 300);
            App.studenteFrame.setLocationRelativeTo(null);

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
            matricolaField = new JTextField(15);
            loginPanel.add(matricolaField, gbc);

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
            App.studenteFrame.add(panel);
        }
    }

    public JButton getStudenteButton() {
        return studenteButton;
    }

    public JPanel getPanel() { return panel; }
    public JLabel getTitleLabel() { return titleLabel; }
    public JPanel getButtonPanel() { return buttonPanel; }
    public  JButton getPrenotazioneButton() { return prenotazioneButton; }
    public  JButton getNotificheButton() { return notificheButton; }
    public JButton getTestButton() { return testButton; }
    public JButton getLogoutButton() { return logoutButton; }
    public JPanel getLogoutPanel() { return logoutPanel;}
    public JButton getLoginButton(){ return loginButton;}
    public JPasswordField getPasswordField() { return passwordField;}
    public JTextField getMatricolaField(){ return matricolaField;}
}

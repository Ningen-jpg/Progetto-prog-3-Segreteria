package View;
import javax.swing.*;
import java.awt.*;

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

    public StudentePanel() {
        setLayout(new BorderLayout(20, 0));
        JPanel panel = new JPanel(new GridBagLayout());
        studenteButton.setFont(new Font("Arial",Font.PLAIN,14));
        studenteButton.setPreferredSize(new Dimension(130,60));
        panel.add(studenteButton);
        add(panel);
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
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public Main() {
        // Configurazione del frame
        super("Login Studente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);

        // Impostazione del layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        // Creazione e aggiunta dei componenti
        add(new JLabel("Username:"));

        usernameField = new JTextField(15);
        add(usernameField);

        add(new JLabel("Password:"));

        passwordField = new JPasswordField(15);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        // Aggiunta del listener per il pulsante
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificaCredenziali();
            }
        });
    }

    private void verificaCredenziali() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Utilizzo della classe LoginStudente
        LoginUtente login = new LoginStudente();
        boolean risultato = login.login(username, password);

        if (risultato) {
            JOptionPane.showMessageDialog(this, "Login effettuato con successo!");
            // Qui puoi aggiungere codice per aprire la finestra principale dell'applicazione
        } else {
            JOptionPane.showMessageDialog(this, "Username o password non validi!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Esecuzione dell'applicazione nel thread EDT
        SwingUtilities.invokeLater(() -> {
            Main loginFrame = new Main();
            loginFrame.setVisible(true);
        });
    }
}




/*public class Main{
    public static void main(String[] args){

        LoginUtente loginButton = new LoginStudente();
        loginButton.login("0124","123cc");

    }

}*/
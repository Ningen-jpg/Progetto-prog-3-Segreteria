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
                LoginUtente login = new LoginStudente();
               // LoginDocente login = new LoginDocente();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean risultato = login.login(username, password);

            }
        });
    }

    public static void main(String[] args) {
        // Esecuzione dell'applicazione nel thread EDT
        /*
        SwingUtilities.invokeLater(() -> {
            Main loginFrame = new Main();
            loginFrame.setVisible(true);
        });
*/
        /*
        System.out.println("inizio inserimento studente");
        Segreteria.aggiungi_studente();
        */
        System.out.println("visualizzo le informazioni di uno studente");
        Segreteria.visualizza_informazioni();


    }
}
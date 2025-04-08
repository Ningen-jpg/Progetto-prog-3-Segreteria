import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        // Creiamo l'observer e il subject per le notifiche in tempo reale
        StudenteObserver observer = new StudenteObserver();
        DocenteSubject docenteSubject = new DocenteSubject();
        docenteSubject.addObserver(observer);

        // Login come docente
        System.out.println("\n=== Accesso come DOCENTE ===");
        System.out.println("Inserisci ID docente: ");
        String idDocente = scanner.nextLine();
        System.out.println("Inserisci password docente: ");
        String pwDocente = scanner.nextLine();

        Utente prof = ConcreteUtenteFactory.getUtente("docente", idDocente, pwDocente, "Filippo", "Bonomi");
        ((Docente) prof).inserisci_voto(idDocente, docenteSubject);
        ((Docente) prof).inserisci_voto(idDocente, docenteSubject);

        // Login come studente
        System.out.println("\n=== Accesso come STUDENTE ===");
        System.out.println("Inserisci matricola studente: ");
        String matricola = scanner.nextLine();
        System.out.println("Inserisci password studente: ");
        String pwStudente = scanner.nextLine();

        Utente stud = ConcreteUtenteFactory.getUtente("studente", matricola, pwStudente, "Alessandro", "Guadagnuolo");
        ((Studente) stud).setObserver(observer);
        ((Studente) stud).valutaVoto();
    }
}
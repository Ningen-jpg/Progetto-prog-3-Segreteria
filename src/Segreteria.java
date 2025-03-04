import javax.swing.*;
import java.sql.*;
import java.util.Scanner;
import java.text.*;

public class Segreteria extends Utente {
    public Segreteria(String id,String password,  String nome, String cognome) {
        super(id,password,nome,cognome);
    }

    public static void aggiungi_studente() {
        Scanner scanner = new Scanner(System.in);

        // Input dei dati dello studente
        System.out.print("Inserisci la matricola: ");
        String matricola = scanner.nextLine();

        System.out.print("Inserisci il nome: ");
        String st_nome = scanner.nextLine();

        System.out.print("Inserisci il cognome: ");
        String st_cognome = scanner.nextLine();

        System.out.print("Inserisci la data di nascita: ");
        Date data_nascita = Date.valueOf(scanner.nextLine());

        System.out.print("Inserisci la residenza: ");
        String st_residenza = scanner.nextLine();

        boolean tasse = false;
        String st_password = "123cc";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Stabilisci la connessione al database
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei"
            );

            // Query unica che inserisce solo se la matricola non esiste
            String query = "INSERT INTO studente (matricola, nome, cognome, data_nascita, residenza, tasse, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (matricola) DO NOTHING";

            // Prepara lo statement
            statement = connection.prepareStatement(query);

            // Imposta i parametri
            statement.setString(1, matricola);
            statement.setString(2, st_nome);
            statement.setString(3, st_cognome);
            statement.setDate(4, data_nascita);
            statement.setString(5, st_residenza);
            statement.setBoolean(6, tasse);
            statement.setString(7, st_password);

            // Esegue l'inserimento
            int rowsAffected = statement.executeUpdate();

            // Verifica il risultato
            if (rowsAffected > 0) {
                System.out.println("Studente aggiunto con successo.");
            } else {
                System.out.println("Studente già esistente. Inserimento non riuscito.");
            }

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiunta dello studente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


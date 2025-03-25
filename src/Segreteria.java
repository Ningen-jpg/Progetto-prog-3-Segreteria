import java.sql.*;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class Segreteria extends Utente {
    public Segreteria(String id,String password,  String nome, String cognome) {
        super(id,password,nome,cognome);
    }

    public String getID(){
        return id;
    }

    //l'aggiunta della nuova tupla prevede l'attributo delle tasse come false di default
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

    public static void visualizza_informazioni(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci la matricola: ");
        String mat_da_ric = scanner.nextLine();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try{
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei"
            );

            String query = "SELECT * FROM studente WHERE matricola = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, mat_da_ric);

            rs = statement.executeQuery();

            if (rs.next()) {
                System.out.println("Matricola: " + rs.getString("matricola"));
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("Cognome: " + rs.getString("cognome"));
                System.out.println("Data di nascita: " + rs.getDate("data_nascita"));
                System.out.println("Residenza: " + rs.getString("residenza"));
                System.out.println("Tasse pagate: " + (rs.getBoolean("tasse")));
            } else {
                System.out.println("Nessuno studente trovato con la matricola specificata.");
            }

        }catch (SQLException e) {
            System.out.println("Errore durante la visualizzazione delle informazioni: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void visualizza_esiti(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome dell'esame: ");
        String esame_da_ric = scanner.nextLine();
        esame_da_ric = esame_da_ric.toUpperCase();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try{
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei"
            );

            String query = "SELECT esame.nome, appello.data, studente.matricola, studente.nome as st_nome, studente.cognome, esito.voto " +
                    "FROM esito " +
                    "JOIN appello ON esito.appello_fk = appello.ID " +
                    "JOIN esame ON appello.esame_fk = esame.ID " +
                    "JOIN studente ON esito.studente_fk = studente.matricola " +
                    "WHERE esame.nome = ?";

            statement = conn.prepareStatement(query);
            statement.setString(1, esame_da_ric);

            rs = statement.executeQuery();

            boolean esitiTrovati = false;
            while (rs.next()) {
                esitiTrovati = true;
                System.out.println("Esame: " + rs.getString("nome"));
                System.out.println("Data Appello: " + rs.getDate("data"));
                System.out.println("Matricola: " + rs.getString("matricola"));
                System.out.println("Nome: " + rs.getString("st_nome"));
                System.out.println("Cognome: " + rs.getString("cognome"));
                System.out.println("Voto: " + rs.getInt("voto"));
                System.out.println("---------------------------");
            }

            if (!esitiTrovati) {
                System.out.println("Nessun esito trovato per la ricerca.");
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la visualizzazione degli esiti: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void visualizza_esiti_corso() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome del corso di studi ");
        String corso_da_ric = scanner.nextLine();
        corso_da_ric = corso_da_ric;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei"
            );

            String query = "SELECT corsodilaurea.nome AS corso_nome, esame.nome AS esame_nome, appello.data, " +
                    "studente.matricola, studente.nome AS studente_nome, studente.cognome, esito.voto " +
                    "FROM esito " +
                    "JOIN appello ON esito.appello_fk = appello.ID " +
                    "JOIN esame ON appello.esame_fk = esame.ID " +
                    "JOIN corsodilaurea ON esame.corso_fk = corsodilaurea.ID " +
                    "JOIN studente ON esito.studente_fk = studente.matricola " +
                    "WHERE corsodilaurea.nome = ? ";

            statement = conn.prepareStatement(query);
            statement.setString(1, corso_da_ric);

            rs = statement.executeQuery();

            boolean esitiTrovati = false;
            while (rs.next()) {
                esitiTrovati = true;
                System.out.println("Corso di Laurea: " + rs.getString("corso_nome"));
                System.out.println("Esame: " + rs.getString("esame_nome"));
                System.out.println("Data Appello: " + rs.getDate("data"));
                System.out.println("Matricola: " + rs.getString("matricola"));
                System.out.println("Nome: " + rs.getString("studente_nome"));
                System.out.println("Cognome: " + rs.getString("cognome"));
                System.out.println("Voto: " + rs.getInt("voto"));
                System.out.println("---------------------------");
            }

            if (!esitiTrovati) {
                System.out.println("Nessun esito trovato per la ricerca.");
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la visualizzazione degli esiti: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

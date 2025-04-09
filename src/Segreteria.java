import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Segreteria extends Utente {
    public Segreteria(String id, String password, String nome, String cognome) {
        super(id, password, nome, cognome);
    }

    public String getID() {
        return id;
    }

    // l'aggiunta della nuova tupla prevede l'attributo delle tasse come false di
    // default
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
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            // Query unica che inserisce solo se la matricola non esiste
            String query = "INSERT INTO studente (matricola, nome, cognome, data_nascita, residenza, tasse, password) "
                    +
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
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String visualizza_informazioni(String mat_da_ric) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        StringBuilder result = new StringBuilder();

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            String query = "SELECT * FROM studente WHERE matricola = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, mat_da_ric);

            rs = statement.executeQuery();

            if (rs.next()) {
                result.append("Matricola: ").append(rs.getString("matricola")).append("\n");
                result.append("Nome: ").append(rs.getString("nome")).append("\n");
                result.append("Cognome: ").append(rs.getString("cognome")).append("\n");
                result.append("Data di nascita: ").append(rs.getDate("data_nascita")).append("\n");
                result.append("Residenza: ").append(rs.getString("residenza")).append("\n");
                result.append("Tasse pagate: ").append(rs.getBoolean("tasse")).append("\n");
            } else {
                result.append("Nessuno studente trovato con la matricola specificata.");
            }

        } catch (SQLException e) {
            result.append("Errore durante la visualizzazione delle informazioni: ").append(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    public static String visualizza_esiti(String esame_da_ric) {
        esame_da_ric = esame_da_ric.toUpperCase();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        StringBuilder result = new StringBuilder();

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            String query = "SELECT esame.nome, appello.data, studente.matricola, studente.nome as st_nome, studente.cognome, esito.voto "
                    +
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
                result.append("Esame: ").append(rs.getString("nome")).append("\n");
                result.append("Data Appello: ").append(rs.getDate("data")).append("\n");
                result.append("Matricola: ").append(rs.getString("matricola")).append("\n");
                result.append("Nome: ").append(rs.getString("st_nome")).append("\n");
                result.append("Cognome: ").append(rs.getString("cognome")).append("\n");
                result.append("Voto: ").append(rs.getString("voto")).append("\n");
                result.append("---------------------------\n");
            }

            if (!esitiTrovati) {
                result.append("Nessun esito trovato per la ricerca.");
            }

        } catch (SQLException e) {
            result.append("Errore durante la visualizzazione degli esiti: ").append(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    public static String visualizza_esiti_corso(String corso_da_ric) {
        corso_da_ric = corso_da_ric.toUpperCase();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        StringBuilder result = new StringBuilder();

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

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
                result.append("Corso di Laurea: ").append(rs.getString("corso_nome")).append("\n");
                result.append("Esame: ").append(rs.getString("esame_nome")).append("\n");
                result.append("Data Appello: ").append(rs.getDate("data")).append("\n");
                result.append("Matricola: ").append(rs.getString("matricola")).append("\n");
                result.append("Nome: ").append(rs.getString("studente_nome")).append("\n");
                result.append("Cognome: ").append(rs.getString("cognome")).append("\n");
                result.append("Voto: ").append(rs.getString("voto")).append("\n");
                result.append("---------------------------\n");
            }

            if (!esitiTrovati) {
                result.append("Nessun esito trovato per la ricerca.");
            }

        } catch (SQLException e) {
            result.append("Errore durante la visualizzazione degli esiti: ").append(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    public static boolean addStudente(String matricola, String nome, String cognome,
            String dataNascita, String password, String residenza, boolean tasse) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            String query = "INSERT INTO studente (matricola, nome, cognome, data_nascita, residenza, tasse, password) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (matricola) DO NOTHING";

            statement = connection.prepareStatement(query);
            statement.setString(1, matricola);
            statement.setString(2, nome);
            statement.setString(3, cognome);
            statement.setDate(4, Date.valueOf(dataNascita));
            statement.setString(5, residenza);
            statement.setBoolean(6, tasse);
            statement.setString(7, password);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object[][] getAllStudenti() {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Object[]> rows = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            String query = "SELECT * FROM studente ORDER BY matricola";
            statement = conn.prepareStatement(query);
            rs = statement.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("matricola"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getDate("data_nascita"),
                        rs.getString("residenza"),
                        rs.getBoolean("tasse")
                };
                rows.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rows.toArray(new Object[0][]);
    }
}

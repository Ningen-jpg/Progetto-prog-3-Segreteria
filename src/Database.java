import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection conn = null;

    // metodo per aprire la connessione
    public static Connection connect() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\aless\\OneDrive\\Documenti\\Università\\Prog 3\\Progetto-prog-3-Segreteria\\database");
            System.out.println("Connessione al database stabilita!");
        } catch (SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }
        return conn;
    }

    // metodo per chiudere la connessione
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connessione al database chiusa!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }



}

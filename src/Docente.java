import java.sql.*;
import java.util.Scanner;

public class Docente extends Utente {
    private Mediator mediator;
    private DocenteSubject docenteSubject;
    public Docente(String id, String password, String nome, String cognome) {
        super(id, password, nome, cognome);
    }

    @Override
    public String getID() {
        return id;
    }

    public void inserisci_appello() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome dell'esame: ");
        String esame_nome = scanner.nextLine();
        System.out.print("Inserisci la data dell'appello (YYYY-MM-DD): ");
        String data_appello = scanner.nextLine();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            // Recuperiamo l'ID dell'esame e il docente associato
            String queryEsame = "SELECT id, docente_fk FROM esame WHERE nome = ?";
            statement = conn.prepareStatement(queryEsame);
            statement.setString(1, esame_nome);
            rs = statement.executeQuery();

            if (!rs.next()) {
                System.out.println("Errore: Esame non trovato.");
                return;
            }

            int esameID = rs.getInt("id");
            String docenteID = rs.getString("docente_fk"); // Docente associato all'esame
            String id = rs.getString("id");
            rs.close();
            statement.close();

            // Controlliamo che il docente loggato sia effettivamente il docente di
            // quell'esame
            if (!this.getID().equals(docenteID)) {
                System.out.println("Errore: Non sei il docente di questo esame. Operazione negata.");
                return;
            }

            // sto facendo l'inserimento
            String queryInserimento = "INSERT INTO appello (id, data, esame_fk, num_prenotati) VALUES (?, ?, ?, ?)";
            statement = conn.prepareStatement(queryInserimento);
            statement.setInt(1, esameID);
            statement.setDate(2, Date.valueOf(data_appello));
            statement.setString(3, id);
            statement.setInt(4, 0);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Appello inserito con successo!");
            } else {
                System.out.println("Errore durante l'inserimento dell'appello.");
            }

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento dell'appello: " + e.getMessage());
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
    }

    //mediator pattern implementato
    public void inserisci_voto(DocenteSubject docenteSubject) {

     mediator.inviaVoto(docenteSubject);
    }
}
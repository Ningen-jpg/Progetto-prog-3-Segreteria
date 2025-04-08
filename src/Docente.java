import java.sql.*;
import java.util.Scanner;

public class Docente extends Utente {
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

    public void inserisci_voto(String id, DocenteSubject docenteSubject) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            System.out.print("Inserisci il nome dell'esame: ");
            String nome_esame = scanner.nextLine().toUpperCase();

            // Recuperiamo tutti gli appelli per questo esame
            String queryEsame = "SELECT esame.id AS id_esame, esame.docente_fk, appello.id AS id_appello, appello.data "
                    +
                    "FROM appello " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE esame.nome = ? " +
                    "ORDER BY appello.data DESC";

            statement = conn.prepareStatement(queryEsame);
            statement.setString(1, nome_esame);
            rs = statement.executeQuery();

            String idEsame = null;
            String docente_fk = null;
            String idAppello = null;
            String dataAppello = null;

            System.out.println("\nAppelli disponibili per " + nome_esame + ":");
            boolean appelloTrovato = false;
            while (rs.next()) {
                appelloTrovato = true;
                String currentIdAppello = rs.getString("id_appello");
                String currentData = rs.getString("data");
                System.out.println(" - ID Appello: " + currentIdAppello + " | Data: " + currentData);

                // Salviamo i dati del primo appello come default
                if (idEsame == null) {
                    idEsame = rs.getString("id_esame");
                    docente_fk = rs.getString("docente_fk");
                }
            }

            if (!appelloTrovato) {
                System.out.println("Esame non trovato!");
                return;
            }

            System.out.print("\nInserisci l'ID dell'appello desiderato: ");
            idAppello = scanner.nextLine();

            // Recupera la data dell'appello selezionato
            String queryData = "SELECT data FROM appello WHERE id = ?";
            statement = conn.prepareStatement(queryData);
            statement.setString(1, idAppello);
            rs = statement.executeQuery();

            if (rs.next()) {
                dataAppello = rs.getString("data");
            }

            // Verifica se il docente loggato è effettivamente il docente dell'esame
            if (!docente_fk.equals(id)) {
                System.out.println("Non sei il docente di questo esame!");
                return;
            }

            // Prima verifichiamo se esistono esiti per questo appello
            String queryVerificaEsiti = "SELECT COUNT(*) as total FROM esito WHERE appello_fk = ? AND voto = '0'";
            statement = conn.prepareStatement(queryVerificaEsiti);
            statement.setString(1, idAppello);
            rs = statement.executeQuery();

            // Visualizziamo gli esiti senza voto
            String query = "SELECT studente.matricola, studente.nome " +
                    "FROM esito " +
                    "JOIN studente ON esito.studente_fk = studente.matricola " +
                    "WHERE esito.voto = '0' AND esito.appello_fk = ?";

            statement = conn.prepareStatement(query);
            statement.setString(1, idAppello);
            rs = statement.executeQuery();

            boolean trovatiStudenti = false;
            System.out.println("Studenti senza voto:");
            while (rs.next()) {
                trovatiStudenti = true;
                System.out.println(" - Matricola: " + rs.getString("matricola") + " | Nome: " + rs.getString("nome"));
            }

            if (!trovatiStudenti) {
                System.out.println("Nessuno studente trovato senza voto per questo appello.");
                // Debug query per vedere tutti gli esiti per questo appello
                String queryTuttiEsiti = "SELECT studente_fk, voto FROM esito WHERE appello_fk = ?";
                statement = conn.prepareStatement(queryTuttiEsiti);
                statement.setString(1, idAppello);
                rs = statement.executeQuery();

                System.out.println("DEBUG: Tutti gli esiti per l'appello " + idAppello + ":");
                while (rs.next()) {
                    System.out.println("Studente: " + rs.getString("studente_fk") + ", Voto: " + rs.getString("voto"));
                }
                return;
            }

            // Selezione dello studente
            System.out.print("Seleziona la matricola dello studente: ");
            String matricola = scanner.nextLine();

            System.out.print("Inserisci il voto: ");
            String voto = scanner.nextLine();

            // Aggiorniamo il voto nella tabella esito
            String insert = "UPDATE esito SET voto = ? WHERE studente_fk = ? AND appello_fk = ?";
            statement.close();
            statement = conn.prepareStatement(insert);
            statement.setString(1, voto);
            statement.setString(2, matricola);
            statement.setString(3, idAppello);
            statement.executeUpdate();

            System.out.println("Voto registrato con successo!");

            // Salva la notifica nel database
            String insertNotifica = "INSERT INTO Notifica (id_notifica, studente_fk, nome_esame, voto, data) VALUES (?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(insertNotifica);
            // Creo un ID univoco combinando l'ID dell'appello e la matricola dello studente
            Integer idNotifica = Integer.parseInt(idAppello);
            statement.setInt(1, idNotifica);
            statement.setString(2, matricola);
            statement.setString(3, nome_esame);
            statement.setString(4, voto);
            statement.setDate(5, Date.valueOf(dataAppello));
            statement.executeUpdate();

            // Notifica lo Studente con Observer (per notifiche in tempo reale)
            docenteSubject.notifyObservers(matricola, nome_esame, voto);

        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
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
}
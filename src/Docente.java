import jdk.jfr.Percentage;

import java.sql.*;
import java.util.Scanner;

public class Docente extends Utente{
    public Docente(String id, String password, String nome, String cognome) {
        super(id, password, nome, cognome);
    }

    @Override
    public String getID()
    {
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
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei"
            );

            //Recuperiamo l'ID dell'esame e il docente associato
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

            //Controlliamo che il docente loggato sia effettivamente il docente di quell'esame
            if (!this.getID().equals(docenteID)) {
                System.out.println("Errore: Non sei il docente di questo esame. Operazione negata.");
                return;
            }

            //sto facendo l'inserimento
            String queryInserimento = "INSERT INTO appello (id, data, esame_fk) VALUES (?, ?, ?)";
            statement = conn.prepareStatement(queryInserimento);
            statement.setInt(1, esameID);
            statement.setDate(2, Date.valueOf(data_appello));
            statement.setString(3, id);

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
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void inserisci_voto(String id) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei"
            );

            System.out.print("Inserisci il nome dell'esame: ");
            String nome_esame = scanner.nextLine();

            //Recuperiamo l'ID dell'Esame e il Docente associato
            String queryEsame = "SELECT esame.id AS id_esame, esame.docente_fk, appello.id AS id_appello " +
                    "FROM appello " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE esame.nome = ?";

            statement = conn.prepareStatement(queryEsame);
            statement.setString(1, nome_esame);
            rs = statement.executeQuery();

            String idEsame = null;
            String docente_fk = null;
            String idAppello = null;

            if (rs.next()) {
                idEsame = rs.getString("id_esame");
                docente_fk = rs.getString("docente_fk");
                idAppello = rs.getString("id_appello");
            } else {
                System.out.println("Esame non trovato!");
                return;
            }

            //Verifica se il docente loggato è effettivamente il docente dell'esame
            if (!docente_fk.equals(id)) {
                System.out.println("Non sei il docente di questo esame!");
                return;
            }

            //Visualizziamo gli esiti senza voto
            String query = "SELECT studente.matricola, studente.nome " +
                    "FROM esito " +
                    "JOIN studente ON esito.studente_fk = studente.matricola " +
                    "WHERE esito.voto IS NULL AND esito.appello_fk = ?";

            statement = conn.prepareStatement(query);
            statement.setString(1, idAppello);
            rs = statement.executeQuery();

            System.out.println("Studenti senza voto:");
            while (rs.next()) {
                System.out.println(" - Matricola: " + rs.getString("matricola") + " | Nome: " + rs.getString("nome"));
            }

            //Selezione dello studente
            System.out.print("🔹 Seleziona la matricola dello studente: ");
            String matricola = scanner.next();

            System.out.print("Inserisci voto: ");
            int voto = scanner.nextInt();

            //Aggiorniamo il voto nella tabella esito
            String insert = "UPDATE esito SET voto = ? WHERE studente_fk = ? AND appello_fk = ?";
            statement = conn.prepareStatement(insert);
            statement.setInt(1, voto);
            statement.setString(2, matricola);
            statement.setString(3, idAppello);
            statement.executeUpdate();

            System.out.println("Voto registrato, attesa conferma dello studente...");

            //Notifica tramite Observer
            StudenteObserver studente = new StudenteObserver(matricola);
            studente.update(matricola, nome_esame, voto);

            //Attende la conferma
            boolean confermato;
            //qui devi richiamare il metodo per ottenere la conferma, chiaro ?

            if (!confermato) {
                System.out.println("Lo Studente ha rifiutato, eliminazione voto...");
                statement = conn.prepareStatement("DELETE FROM esito WHERE studente_fk = ? AND appello_fk = ?");
                statement.setString(1, matricola);
                statement.setString(2, idAppello);
                statement.executeUpdate();
            } else {
                System.out.println("Lo Studente ha confermato il voto!");
                //Aggiorna il campo conferma nella tabella esito
                String confermaQuery = "UPDATE esito SET conferma = TRUE WHERE studente_fk = ? AND appello_fk = ?";
                statement = conn.prepareStatement(confermaQuery);
                statement.setString(1, matricola);
                statement.setString(2, idAppello);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
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
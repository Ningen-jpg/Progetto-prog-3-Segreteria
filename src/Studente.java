import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Studente extends Utente {
    private String matricola; // chiave primaria
    private Date dataNascita;
    private String residenza;
    private List<Esame> esami;
    private CorsoDiLaurea pianoDiStudi;
    private List<Esame> esamiSuperati;
    private List<Esame> esamiSostenuti;
    private List<Esame> testCompletati;
    private boolean tasse;
    private String password;
    private StudenteObserver observer;
    private Mediator mediator;
    public void setObserver(StudenteObserver observer) {
        this.observer = observer;
    }

    public Studente(String matricola, String password, String nome, String cognome) {
        super(matricola, password, nome, cognome);
        this.matricola = matricola;
        this.mediator = new NotificationService();
    }

    public String getID() {
        return id;
    }
    // Getters e setters per tutti gli attributi

    public String getMatricola() {
        return matricola;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getResidenza() {
        return residenza;
    }

    public void setResidenza(String residenza) {
        this.residenza = residenza;
    }

    public List<Esame> getEsami() {
        return esami;
    }

    public void setEsami(List<Esame> esami) {
        this.esami = esami;
    }

    public CorsoDiLaurea getPianoDiStudi() {
        return pianoDiStudi;
    }

    public void setPianoDiStudi(CorsoDiLaurea pianoDiStudi) {
        this.pianoDiStudi = pianoDiStudi;
    }

    public List<Esame> getEsamiSuperati() {
        return esamiSuperati;
    }

    public void setEsamiSuperati(List<Esame> esamiSuperati) {
        this.esamiSuperati = esamiSuperati;
    }

    public List<Esame> getEsamiSostenuti() {
        return esamiSostenuti;
    }

    public void setEsamiSostenuti(List<Esame> esamiSostenuti) {
        this.esamiSostenuti = esamiSostenuti;
    }

    public List<Esame> getTestCompletati() {
        return testCompletati;
    }

    public void setTestCompletati(List<Esame> testCompletati) {
        this.testCompletati = testCompletati;
    }

    public boolean isTasse() {
        return tasse;
    }

    public void setTasse(boolean tasse) {
        this.tasse = tasse;
    }

    // public void setPassword(String password) {}

    public String getPassword() {
        return password;
    }

    public void effettuaPrenotazione(String matricola) {

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            String nome_esame = JOptionPane.showInputDialog(null, "Inserisci il nome dell'esame:").toUpperCase();

            // Recuperiamo tutti gli appelli disponibili per quell'esame
            String queryAppelli = "SELECT appello.id, appello.data FROM appello " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE esame.nome = ?";

            statement = conn.prepareStatement(queryAppelli);
            statement.setString(1, nome_esame);
            rs = statement.executeQuery();
            Boolean appelli_trovati = false;
            StringBuilder appelli = new StringBuilder("Appelli disponibili:\n");
            while (rs.next()) {
                appelli_trovati = true;
                appelli.append(" - Id: ").append(rs.getString("id")).append(" | Data: ")
                        .append(rs.getString("data")).append("\n");
            }

            if (!appelli_trovati) {
                JOptionPane.showMessageDialog(null,"Non ci sono ancora appelli disponibili per questo esame.");
                return;
            }

            String appelliDisplay = appelli.toString();
            String idAppelloScelto = (String) JOptionPane.showInputDialog(null, appelliDisplay,
                    "Seleziona l'appello (ID):", JOptionPane.QUESTION_MESSAGE);

            // Controlliamo il numero di prenotazioni già effettuate per questo appello

            String queryNumPrenotati = "SELECT num_prenotati FROM appello WHERE id = ?";

            statement = conn.prepareStatement(queryNumPrenotati);
            statement.setString(1, idAppelloScelto);
            rs = statement.executeQuery();

            int numPrenotati = 0;
            if (rs.next()) {
                numPrenotati = rs.getInt("num_prenotati");
            }

            if (numPrenotati >= 50) {
                JOptionPane.showMessageDialog(null,"Il numero massimo di prenotazioni per questo appello è stato raggiunto.");
                return;
            }

            // Verifichiamo se lo studente è già prenotato
            String queryCheck = "SELECT * FROM Prenotazione WHERE appello_fk = ? AND studente_fk = ?";
            statement = conn.prepareStatement(queryCheck);
            statement.setString(1, idAppelloScelto);
            statement.setString(2, matricola);
            rs = statement.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,"Sei già prenotato per questo appello!");
                return;
            }

            // Inseriamo la prenotazione nella tabella Prenotazioni
            String queryInsert = "INSERT INTO Prenotazione (appello_fk, studente_fk) VALUES (?, ?)";
            statement = conn.prepareStatement(queryInsert);
            statement.setString(1, idAppelloScelto);
            statement.setString(2, matricola);
            statement.executeUpdate();

            // Aggiorniamo il numero di prenotati nell'appello
            String queryUpdateNumPrenotati = "UPDATE appello SET num_prenotati = num_prenotati + 1 WHERE id = ?";
            statement = conn.prepareStatement(queryUpdateNumPrenotati);
            statement.setString(1, idAppelloScelto);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(null,"Prenotazione effettuata con successo!");
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

    public void valutaVoto(String matricola) {

        mediator.gestisciNotifica(matricola);
    }

    // Implementazione del metodo effettuaTest
    public void effettuaTest() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            // Recupera gli esami per cui lo studente è prenotato
            String queryPrenotazioni = "SELECT esame.id, esame.nome, appello.id AS id_appello, appello.data " +
                    "FROM Prenotazione " +
                    "JOIN appello ON Prenotazione.appello_fk = appello.id " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE Prenotazione.studente_fk = ? " +
                    "ORDER BY appello.data";

            statement = conn.prepareStatement(queryPrenotazioni);
            statement.setString(1, this.matricola);
            rs = statement.executeQuery();

            List<String> appelliPrenotati = new ArrayList<>();
            System.out.println("\n=== Esami per cui sei prenotato ===");
            boolean trovatiEsami = false;

            while (rs.next()) {
                trovatiEsami = true;
                String idEsame = rs.getString("id");
                String nomeEsame = rs.getString("nome");
                String idAppello = rs.getString("id_appello");
                String dataAppello = rs.getString("data");

                appelliPrenotati.add(idAppello);
                System.out.println("ID Appello: " + idAppello + " | Esame: " + nomeEsame + " (ID: " + idEsame
                        + ") | Data appello: " + dataAppello);
            }

            if (!trovatiEsami) {
                System.out.println("Non sei prenotato per nessun esame. Effettua prima una prenotazione.");
                return;
            }

            System.out.print("\nInserisci l'ID dell'appello per cui vuoi effettuare il test: ");
            String idAppelloScelto = scanner.nextLine();

            if (!appelliPrenotati.contains(idAppelloScelto)) {
                System.out.println("ID appello non valido o non sei prenotato per questo appello!");
                return;
            }

            // Recupera l'ID dell'esame per questo appello
            String queryEsame = "SELECT esame.id, esame.nome FROM Prenotazione " +
                    "JOIN appello ON Prenotazione.appello_fk = appello.id " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE Prenotazione.studente_fk = ? AND appello.id = ?";
            statement = conn.prepareStatement(queryEsame);
            statement.setString(1, this.matricola);
            statement.setString(2, idAppelloScelto);
            rs = statement.executeQuery();

            if (!rs.next()) {
                System.out.println("Errore: esame non trovato!");
                return;
            }
            String idEsame = rs.getString("id");
            String nomeEsame = rs.getString("nome");

            // Chiedi allo studente se vuole effettuare il test
            System.out.print("\nVuoi effettuare il test per " + nomeEsame + "? (si/no): ");
            String risposta = scanner.nextLine().toLowerCase();

            if (risposta.equals("si")) {
                // Inserisci l'esito con voto = 0 e conferma = false
                String insertEsito = "INSERT INTO esito (appello_fk, studente_fk, voto, conferma) VALUES (?, ?, '0', false)";
                statement = conn.prepareStatement(insertEsito);
                statement.setString(1, idAppelloScelto);
                statement.setString(2, this.matricola);
                statement.executeUpdate();

                // Rimuovi la prenotazione
                String deletePrenotazione = "DELETE FROM Prenotazione WHERE appello_fk = ? AND studente_fk = ?";
                statement = conn.prepareStatement(deletePrenotazione);
                statement.setString(1, idAppelloScelto);
                statement.setString(2, this.matricola);
                statement.executeUpdate();

                System.out.println(
                        "Hai effettuato il test e sei stato registrato per l'esame. Attendi il voto del docente.");
            } else if (risposta.equals("no")) {
                // Rimuovi solo la prenotazione
                String deletePrenotazione = "DELETE FROM Prenotazione WHERE appello_fk = ? AND studente_fk = ?";
                statement = conn.prepareStatement(deletePrenotazione);
                statement.setString(1, idAppelloScelto);
                statement.setString(2, this.matricola);
                statement.executeUpdate();

                System.out.println("Hai annullato la prenotazione per questo esame.");
            } else {
                System.out.println("Risposta non valida. Operazione annullata.");
            }

        } catch (SQLException e) {
            System.out.println("Errore durante l'esecuzione del test: " + e.getMessage());
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

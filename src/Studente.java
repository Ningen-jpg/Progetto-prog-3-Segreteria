import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.util.List;


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
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            // Recupera gli esami per cui lo studente è prenotato
            String queryPrenotazioni = "SELECT esame.nome, appello.id AS id_appello, appello.data " +
                    "FROM Prenotazione " +
                    "JOIN appello ON Prenotazione.appello_fk = appello.id " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE Prenotazione.studente_fk = ? " +
                    "ORDER BY appello.data";

            statement = conn.prepareStatement(queryPrenotazioni);
            statement.setString(1, this.matricola);
            rs = statement.executeQuery();

            JOptionPane.showMessageDialog(null,"Esami per cui sei prenotato:\n\n");
            JPanel pannelloPrenotazioni = new JPanel();
            pannelloPrenotazioni.setLayout(new BoxLayout(pannelloPrenotazioni, BoxLayout.Y_AXIS));
            boolean ciSonoPrenotazioni = false;
            final JDialog dialog = new JDialog();
            while (rs.next()) {
                ciSonoPrenotazioni = true;
                String id_appello = rs.getString("id_appello");
                String nomeEsame = rs.getString("nome");
                String dataAppello = rs.getString("data");

                String stringEsami = " | Esame: " + nomeEsame + " | Data Appello: " + dataAppello;

                JButton prenotazione = new JButton(stringEsami);

                prenotazione.addActionListener(e -> {
                    // Chiedi allo studente se vuole effettuare il test
                    String[] opzioni = {"Effettua il test", "Cancella prenotazione"};
                    String messaggio = "Vuoi effettuare il test ?";
                    int scelta = JOptionPane.showOptionDialog(
                            dialog,
                            messaggio,
                            "Effettua test",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opzioni,
                            opzioni[0]
                    );

                    if (scelta == JOptionPane.YES_OPTION) {
                        try {
                            Connection newconn = DriverManager.getConnection("jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");
                            PreparedStatement stmt = null;

                            // Inserisci l'esito con voto = 0 e conferma = false
                            String insertEsito = "INSERT INTO esito (appello_fk, studente_fk, voto, conferma) VALUES (?, ?, '0', false)";
                            stmt = newconn.prepareStatement(insertEsito);
                            stmt.setString(1, id_appello);
                            stmt.setString(2, this.matricola);
                            stmt.executeUpdate();

                            // Rimuovi la prenotazione
                            String deletePrenotazione = "DELETE FROM Prenotazione WHERE appello_fk = ? AND studente_fk = ?";
                            stmt = newconn.prepareStatement(deletePrenotazione);
                            stmt.setString(1, id_appello);
                            stmt.setString(2, this.matricola);
                            stmt.executeUpdate();

                            JOptionPane.showMessageDialog(dialog,"Hai effettuato il test e sei stato registrato per l'esame.");
                            pannelloPrenotazioni.remove(prenotazione);

                            // Aggiorna la UI
                            pannelloPrenotazioni.revalidate();
                            pannelloPrenotazioni.repaint();
                        } catch(SQLException ex){
                            ex.printStackTrace();
                        }
                    } else if(scelta == JOptionPane.NO_OPTION){
                        try{
                            Connection newconn = DriverManager.getConnection("jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");
                            PreparedStatement stmt = null;
                            // Rimuovi solo la prenotazione
                            String deletePrenotazione = "DELETE FROM Prenotazione WHERE appello_fk = ? AND studente_fk = ?";
                            stmt = newconn.prepareStatement(deletePrenotazione);
                            stmt.setString(1, id);
                            stmt.setString(2, this.matricola);
                            stmt.executeUpdate();

                            JOptionPane.showMessageDialog(dialog,"Hai annullato la prenotazione per questo esame.");
                        } catch(SQLException ex){
                            ex.printStackTrace();
                        }
                        pannelloPrenotazioni.remove(prenotazione);

                        // Aggiorna la UI
                        pannelloPrenotazioni.revalidate();
                        pannelloPrenotazioni.repaint();
                    }
                    else {JOptionPane.showMessageDialog(dialog,"Risposta non valida. Operazione annullata.");}
                });
                pannelloPrenotazioni.add(prenotazione);
            }
            if(ciSonoPrenotazioni){
                dialog.setTitle("Prenotazioni");
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.getContentPane().add(new JScrollPane(pannelloPrenotazioni));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
            else { JOptionPane.showMessageDialog(null,"Non ci sono notifiche");}
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Errore durante il test, riprova." + e.getMessage());
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

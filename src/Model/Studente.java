package Model;
import Mediator.Mediator;
import Mediator.NotificationService;
import javax.swing.*;
import java.sql.*;
@SuppressWarnings("ALL")

public class Studente extends Utente {
    private String matricola; // chiave primaria
    private String password;
    private Mediator mediator;

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

            String query = "SELECT appello.id, esame.nome AS nome_esame, appello.data " +
                    "FROM appello " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE appello.id NOT IN ( " +
                    "SELECT appello_fk FROM esito WHERE studente_fk = ? " +
                    "UNION " +
                    "SELECT appello_fk FROM prenotazione WHERE studente_fk = ?)";

            statement = conn.prepareStatement(query);
            statement.setString(1, matricola);
            statement.setString(2, matricola);
            rs = statement.executeQuery();

            JPanel appelliPanel = new JPanel();
            appelliPanel.setLayout(new BoxLayout(appelliPanel, BoxLayout.Y_AXIS));
            boolean ciSonoAppelli = false;

            while (rs.next()) {
                ciSonoAppelli = true;
                String idAppello = rs.getString("id");
                String nomeEsame = rs.getString("nome_esame");
                String data = rs.getString("data");

                String label = nomeEsame + " | Data: " + data;
                JButton appelloButton = new JButton(label);

                appelloButton.addActionListener(e -> {
                    int scelta = JOptionPane.showConfirmDialog(null,
                            "Vuoi prenotarti all'appello di " + nomeEsame + " del " + data + "?",
                            "Conferma Prenotazione",
                            JOptionPane.YES_NO_OPTION);

                    if (scelta == JOptionPane.YES_OPTION) {
                        Connection newConn = null;
                        PreparedStatement insertStmt = null;
                        PreparedStatement updateStmt = null;

                        try {
                            newConn = DriverManager.getConnection(
                                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

                            String queryInsert = "INSERT INTO Prenotazione (appello_fk, studente_fk) VALUES (?, ?)";
                            insertStmt = newConn.prepareStatement(queryInsert);
                            insertStmt.setString(1, idAppello);
                            insertStmt.setString(2, matricola);
                            insertStmt.executeUpdate();

                            String queryUpdate = "UPDATE appello SET num_prenotati = num_prenotati + 1 WHERE id = ?";
                            updateStmt = newConn.prepareStatement(queryUpdate);
                            updateStmt.setString(1, idAppello);
                            updateStmt.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Prenotazione effettuata con successo!");

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Errore durante la prenotazione: " + ex.getMessage());
                            ex.printStackTrace();
                        } finally {
                            try {
                                if (insertStmt != null) insertStmt.close();
                                if (updateStmt != null) updateStmt.close();
                                if (newConn != null) newConn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

                appelliPanel.add(appelloButton);
            }

            if (ciSonoAppelli) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Appelli Prenotabili");
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.getContentPane().add(new JScrollPane(appelliPanel));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Non ci sono appelli al momento.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore: " + e.getMessage());
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
                    Connection newconn = null;
                    PreparedStatement stmt = null;
                    if (scelta == JOptionPane.YES_OPTION) {
                        try {
                            newconn = DriverManager.getConnection("jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

                            // Inserisci l'esito con voto = 0 e conferma = false
                            String insertEsito = "INSERT INTO esito (appello_fk, studente_fk, voto, conferma) VALUES (?, ?, '0', false)";
                            stmt = newconn.prepareStatement(insertEsito);
                            stmt.setString(1, id_appello);
                            stmt.setString(2, this.matricola);
                            stmt.executeUpdate();
                            stmt.close();

                            // Rimuovi la prenotazione
                            String deletePrenotazione = "DELETE FROM Prenotazione WHERE appello_fk = ? AND studente_fk = ?";
                            stmt = newconn.prepareStatement(deletePrenotazione);
                            stmt.setString(1, id_appello);
                            stmt.setString(2, this.matricola);
                            stmt.executeUpdate();
                            stmt.close();

                            JOptionPane.showMessageDialog(dialog,"Hai effettuato il test e sei stato registrato per l'esame.");
                            pannelloPrenotazioni.remove(prenotazione);

                            // Aggiorna la UI
                            pannelloPrenotazioni.revalidate();
                            pannelloPrenotazioni.repaint();
                        } catch(SQLException ex){
                            ex.printStackTrace();
                        }finally {
                            try {
                                if (stmt != null) stmt.close();
                                if (newconn != null) newconn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else if(scelta == JOptionPane.NO_OPTION){
                        try{
                            newconn = DriverManager.getConnection("jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");
                            // Rimuovi solo la prenotazione
                            String deletePrenotazione = "DELETE FROM Prenotazione WHERE appello_fk = ? AND studente_fk = ?";
                            stmt = newconn.prepareStatement(deletePrenotazione);
                            stmt.setString(1, id_appello);
                            stmt.setString(2, this.matricola);
                            stmt.executeUpdate();

                            JOptionPane.showMessageDialog(dialog,"Hai annullato la prenotazione per questo esame.");
                        } catch(SQLException ex){
                            ex.printStackTrace();
                        }finally {
                            try {
                                if (stmt != null) stmt.close();
                                if (newconn != null) newconn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                        pannelloPrenotazioni.remove(prenotazione);
                        // Aggiorna la UI
                        pannelloPrenotazioni.revalidate();
                        pannelloPrenotazioni.repaint();
                    }
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

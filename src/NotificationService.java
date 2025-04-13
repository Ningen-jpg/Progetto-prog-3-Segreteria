import java.sql.*;
import java.sql.Date;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class NotificationService implements Mediator {
    private Map<String, Observer> partecipanti = new HashMap<>();
    StudenteObserver observer = new StudenteObserver();
    // per aggiungere un nuovo osservatore
    public void registraPartecipante(String id, Observer observer) {
        partecipanti.put(id, observer);
    }

    // verrà richiamato dai Docenti
    @Override
    public void inviaVoto(DocenteSubject docenteSubject, String docenteID) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            // Recuperiamo il nome e cognome del docente loggato
            String queryDocente = "SELECT nome, cognome FROM docente WHERE id = ?";
            statement = conn.prepareStatement(queryDocente);
            statement.setString(1, docenteID);
            rs = statement.executeQuery();

            if (rs.next()) {
                String nomeDocente = rs.getString("nome");
                String cognomeDocente = rs.getString("cognome");
            }

            // Recuperiamo tutti gli esami per questo docente
            String queryEsamiDocente = "SELECT esame.id, esame.nome FROM esame WHERE esame.docente_fk = ?";
            statement = conn.prepareStatement(queryEsamiDocente);
            statement.setString(1, docenteID);
            rs = statement.executeQuery();

            StringBuilder esamiList = new StringBuilder("Esami disponibili per il docente:\n");
            while (rs.next()) {
                String idEsame = rs.getString("id");
                String nomeEsameDb = rs.getString("nome");
                esamiList.append(" - ID Esame: ").append(idEsame).append(" | Nome: ").append(nomeEsameDb).append(" \n");
            }

            if (esamiList.length() == 0) {
                JOptionPane.showMessageDialog(null, "Nessun esame trovato per questo docente.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostra gli esami in una finestra di dialogo
            String esamiDisplay = esamiList.toString();
            String esameSelezionato = (String) JOptionPane.showInputDialog(null, esamiDisplay, "Seleziona Esame",
                    JOptionPane.QUESTION_MESSAGE);

            if (esameSelezionato == null || esameSelezionato.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operazione annullata", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Recuperiamo gli appelli per l'esame selezionato
            String queryAppelli = "SELECT appello.id AS id_appello, appello.data " +
                    "FROM appello " +
                    "WHERE appello.esame_fk = ? " +
                    "ORDER BY appello.data DESC";
            statement = conn.prepareStatement(queryAppelli);
            statement.setString(1, esameSelezionato);
            rs = statement.executeQuery();

            String idAppello = null;  // inizializza la variabile

            while (rs.next()) {
                idAppello = rs.getString("id_appello");
            }

            // Verifica se il docente loggato è effettivamente il docente di questo esame
            String queryDocenteAppello = "SELECT docente_fk FROM esame WHERE id = ?";
            statement = conn.prepareStatement(queryDocenteAppello);
            statement.setString(1, esameSelezionato);
            rs = statement.executeQuery();

            if (rs.next()) {
                String docente_fk = rs.getString("docente_fk");
                if (!docente_fk.equals(docenteID)) {
                    System.out.println("Non sei il docente di questo esame!");
                    return;
                }
            }

            // Prima verifichiamo se esistono esiti per questo appello
            String queryVerificaEsiti = "SELECT COUNT(*) as total FROM esito WHERE appello_fk = ? AND voto = '0'";
            statement = conn.prepareStatement(queryVerificaEsiti);
            statement.setString(1, idAppello);
            rs = statement.executeQuery();

            // Visualizziamo gli esiti senza voto
            String queryEsiti = "SELECT studente.matricola, studente.nome, appello.data, esame.nome AS nome_esame " +
                    "FROM esito " +
                    "JOIN studente ON esito.studente_fk = studente.matricola " +
                    "JOIN appello ON esito.appello_fk = appello.id " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE esito.voto = '0' AND esito.appello_fk = ?";
            statement = conn.prepareStatement(queryEsiti);
            statement.setString(1, idAppello);
            rs = statement.executeQuery();

            String dataTrovata = null;
            String esameTrovato = null;

            boolean trovatiStudenti = false;
            StringBuilder studentiList = new StringBuilder("Studenti senza voto:\n");
            while (rs.next()) {
                trovatiStudenti = true;
                studentiList.append(" - Matricola: ").append(rs.getString("matricola")).append(" | Nome: ")
                        .append(rs.getString("nome")).append("\n");

                dataTrovata = rs.getString("data");
                esameTrovato = rs.getString("nome_esame");
            }


            if (!trovatiStudenti) {
                JOptionPane.showMessageDialog(null, "Nessuno studente trovato senza voto per questo appello.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostra gli studenti senza voto
            String studentiDisplay = studentiList.toString();
            String matricolaSelezionata = (String) JOptionPane.showInputDialog(null, studentiDisplay,
                    "Seleziona Studente", JOptionPane.QUESTION_MESSAGE);

            if (matricolaSelezionata == null || matricolaSelezionata.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operazione annullata", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Aggiorniamo il voto nella tabella esito
            String updateEsito = "UPDATE esito SET voto = ? WHERE studente_fk = ? AND appello_fk = ?";
            String voto = (String) JOptionPane.showInputDialog(null, "Inserisci il voto dello studente:", JOptionPane.QUESTION_MESSAGE);

            statement = conn.prepareStatement(updateEsito);
            statement.setString(1, voto);
            statement.setString(2, matricolaSelezionata);
            statement.setString(3, idAppello);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Voto registrato con successo!", "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
            Date d = (Date.valueOf(dataTrovata));

            // Salva la notifica nel database
            String insertNotifica = "INSERT INTO Notifica (id_notifica, studente_fk, voto, data, nome_esame) VALUES (?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(insertNotifica);

            statement.setString(1, idAppello);
            statement.setString(2, matricolaSelezionata);
            statement.setString(3, voto);
            statement.setDate(4, d);
            statement.setString(5, esameTrovato);
            statement.executeUpdate();

            // Notifica lo Studente con Observer (per notifiche in tempo reale)
            docenteSubject.notifyObservers(matricolaSelezionata, esameTrovato, voto);

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

    @Override
    public void gestisciNotifica(String matricola) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            String queryNotifiche = "SELECT notifica.id_notifica AS id, notifica.voto, notifica.data, notifica.nome_esame " +
                    "FROM notifica " +
                    "WHERE notifica.studente_fk = ?";
            statement = conn.prepareStatement(queryNotifiche);
            statement.setString(1, matricola);
            rs = statement.executeQuery();

            JPanel notifichePanel = new JPanel();
            notifichePanel.setLayout(new BoxLayout(notifichePanel, BoxLayout.Y_AXIS)); // per metterli in verticale
            Boolean ciSonoNotifiche = false;
            while (rs.next()) {
                ciSonoNotifiche = true;
                String id = rs.getString("id");
                String nomeEsame = rs.getString("nome_esame");
                String voto = rs.getString("voto");
                String data = rs.getString("data");

                String notifica = "Esame: " + nomeEsame + " | Voto: " + voto + " | Data: " + data;
                JButton notificaButton = new JButton(notifica);

                notificaButton.addActionListener(e -> {
                    JOptionPane.showMessageDialog(null,
                            "Notifica selezionata:\nID: " + id);
                    String[] opzioni = {"Accetta", "Rifiuta"};
                    String messaggio = "Cosa vuoi fare ?";
                    int scelta = JOptionPane.showOptionDialog(
                            null,
                            messaggio,
                            "Gestione Voto",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opzioni,
                            opzioni[0]
                    );

                    if (scelta == JOptionPane.YES_OPTION) {
                        String queryAppello = "SELECT appello.id FROM appello " +
                                "JOIN esame ON appello.esame_fk = esame.id " +
                                "WHERE esame.nome = ? AND appello.data::date = ?::date";

                        try {
                            Connection newConn = DriverManager.getConnection(
                                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");
                            PreparedStatement stmtAppello = newConn.prepareStatement(queryAppello);
                            stmtAppello.setString(1, nomeEsame);
                            stmtAppello.setString(2, data);
                            ResultSet rsAppello = stmtAppello.executeQuery();

                            if (!rsAppello.next()) {
                                JOptionPane.showMessageDialog(null, "Errore: non trovato l'appello corrispondente.");
                                return;
                            }

                            String idAppello = rsAppello.getString("id");

                            String queryUpdateConferma = "UPDATE Esito SET conferma = TRUE WHERE appello_fk = ? AND studente_fk = ?";
                            PreparedStatement stmtUpdate = newConn.prepareStatement(queryUpdateConferma);
                            stmtUpdate.setString(1, idAppello);
                            stmtUpdate.setString(2, matricola);
                            stmtUpdate.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Hai accettato il voto. Il campo 'conferma' è stato aggiornato a TRUE.");

                            String deleteNotifica = "DELETE FROM Notifica WHERE id_notifica = ?";
                            PreparedStatement stmtDelete = newConn.prepareStatement(deleteNotifica);
                            stmtDelete.setString(1, id);
                            stmtDelete.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else if (scelta == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(null, "Hai rifiutato il voto.");
                        try {
                            Connection newConn = DriverManager.getConnection(
                                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");
                            String deleteNotifica = "DELETE FROM Notifica WHERE id_notifica = ?";
                            PreparedStatement stmtDelete = newConn.prepareStatement(deleteNotifica);
                            stmtDelete.setString(1, id);
                            stmtDelete.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                notifichePanel.add(notificaButton);

                // Notifica anche l'observer per mantenere lo stato in memoria aggiornato
                if (observer != null) {
                    List<Notifica> notifiche = observer.getNotifiche(matricola);
                    Iterator<Notifica> iterator = notifiche.iterator();
                    while (iterator.hasNext()) {
                        Notifica n = iterator.next();
                        if (n.getEsame().equals(nomeEsame) && n.getVoto().equals(voto) && n.getData().equals(data)) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            if(ciSonoNotifiche){
                JDialog dialog = new JDialog();
                dialog.setTitle("Notifiche");
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.getContentPane().add(new JScrollPane(notifichePanel));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
            else { JOptionPane.showMessageDialog(null,"Non ci sono notifiche");}
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

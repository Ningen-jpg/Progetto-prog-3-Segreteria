package Model;

import Mediator.Mediator;
import Observer.DocenteSubject;
import Mediator.NotificationService;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.Random;

@SuppressWarnings("ALL")

public class Docente extends Utente {
    private Mediator mediator;
    private DocenteSubject docenteSubject;

    public Docente(String id, String password, String nome, String cognome) {

        super(id, password, nome, cognome);
        this.mediator = new NotificationService();
    }

    @Override
    public String getID() {
        return id;
    }

    public void inserisci_appello() {
        // Utilizzo JOptionPane invece di Scanner per l'interfaccia grafica
        String esame_nome = JOptionPane.showInputDialog(null, "Inserisci il nome dell'esame:");
        if (esame_nome == null || esame_nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operazione annullata", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        esame_nome = esame_nome.toUpperCase();

        String data_appello = JOptionPane.showInputDialog(null, "Inserisci la data dell'appello (YYYY-MM-DD):");
        if (data_appello == null || data_appello.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operazione annullata", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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
                JOptionPane.showMessageDialog(null, "Errore: Esame non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String esameID = rs.getString("id");
            String docenteID = rs.getString("docente_fk"); // Model.Docente associato all'esame
            String id = rs.getString("id");
            rs.close();
            statement.close();

            // Controlliamo che il docente loggato sia effettivamente il docente di
            // quell'esame
            if (!this.getID().equals(docenteID)) {
                JOptionPane.showMessageDialog(null, "Errore: Non sei il docente di questo esame. Operazione negata.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // sto facendo l'inserimento
            String queryInserimento = "INSERT INTO appello (id, data, esame_fk, num_prenotati) VALUES (?, ?, ?, ?)";
            statement = conn.prepareStatement(queryInserimento);
            String appelloID = esameID + "-" + (new Random().nextInt(1000));
            statement.setString(1, appelloID);
            statement.setDate(2, Date.valueOf(data_appello));
            statement.setString(3, id);
            statement.setInt(4, 0);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Appello inserito con successo!", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Errore durante l'inserimento dell'appello.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore durante l'inserimento dell'appello: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
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

    // MEDIATOR PATTERN implementato
    /*
    L'applicazione del Mediator.Mediator Pattern viene fatta solamente su inserisci_voto perchè utilizziamo
    il mediator per mediare le notifiche dell'observer.
    Inserisci_appello invece, non richiede observer e quindi non utilizziamo il mediator.
    SCELTA PROGETTUALE
     */
    public void inserisci_voto(DocenteSubject docenteSubject) {
        mediator.inviaVoto(docenteSubject, this.getID());
    }
}
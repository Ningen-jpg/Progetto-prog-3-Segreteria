import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocenteSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();

    private static final String DB_URL = "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_Y5gjymttI8vcX96hEei";

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String matricola, String esame, String voto) {
        String data = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT appello.data FROM appello " +
                    "JOIN esame ON appello.esame_fk = esame.id " +
                    "WHERE esame.nome = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, esame);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                data = rs.getDate("data").toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Observer observer : observers) {
            observer.update(matricola, esame, voto, data);
        }
    }
}
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
@SuppressWarnings("ALL")

public class LoginDocente implements LoginUtente {

    @Override
    public boolean login(String username, String password) {
        // Connection to the server
        try (final Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM docente WHERE id = ? AND password = ?")) {

            // Set the parameters for the prepared statement
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "Login effettuato con successo!");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Username o password non validi!", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore di connessione!!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void logout() {
        JOptionPane.showMessageDialog(null, "Disconnessione docente effettuata con successo", "Logout",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
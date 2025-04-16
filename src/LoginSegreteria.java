import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
@SuppressWarnings("ALL")

public class LoginSegreteria implements LoginUtente {

    @Override
    public boolean login(String id, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Connection to the server
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");

            statement = connection.prepareStatement(
                    "SELECT * FROM segreteria WHERE id = ? AND password = ?");

            // Set the parameters for the prepared statement
            statement.setString(1, id);
            statement.setString(2, password);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Login effettuato con successo!");

                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Username o password non validi!", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println("Errore di connessione!!");
            e.printStackTrace();
        } finally {
            // Chiusura esplicita delle risorse
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void logout() {
        JOptionPane.showMessageDialog(null, "Disconnessione segreteria effettuata con successo", "Logout",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
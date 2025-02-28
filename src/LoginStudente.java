import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class LoginStudente implements LoginUtente {

    @Override
    public void login(String username, String password) {
        // Connection to the server
        try (final Connection connection = DriverManager.getConnection ("jdbc:postgresql://programmazione3-programmazione3.j.aivencloud.com:19840/defaultdb?ssl=require&user=avnadmin&password=AVNS_Y5gjymttI8vcX96hEei");
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM studente WHERE matricola = ? AND password = ?")) {

            // Set the parameters for the prepared statement
            statement.setString(1, username);
            statement.setString(2, password);


            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Invalid username or password.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failure!!.");
            e.printStackTrace();
        }
    }
}
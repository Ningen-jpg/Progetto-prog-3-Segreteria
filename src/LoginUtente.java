import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
Implementazione dello Strategy Pattern
Questa è l'interfaccia che consente di implementare il pattern. Successivamente avremo anche le altre 3 classi
per il login
 */
public interface LoginUtente {

    boolean login(String username, String password);

    void logout();
}

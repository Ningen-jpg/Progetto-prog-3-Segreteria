/*
    Interfaccia Strategy Pattern del login

 */
public interface LoginUtente {

    boolean login(String username, String password);

    void logout();
}

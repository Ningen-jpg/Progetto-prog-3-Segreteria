package Model;

@SuppressWarnings("ALL")
public abstract class Utente {
    protected String id;
    private String nome;
    private String cognome;
    private String password;

    // factory method pattern
    public Utente(String id, String password, String nome, String cognome) {
        this.id = id;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }
    public abstract String getID();


}
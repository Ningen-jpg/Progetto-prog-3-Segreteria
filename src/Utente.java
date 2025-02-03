public abstract class Utente {
    private String id;
    private String nome;
    private String cognome;

    //factory method pattern
    public Utente(String id, String nome, String cognome) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
    }

    //secondo costruttore per la segreteria
    public Utente(String nome, String cognome) {
    }
}
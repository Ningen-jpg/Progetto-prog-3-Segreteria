public abstract class Utente {
    private int id;
    private String nome;
    private String cognome;

    public Utente(int id, String nome, String cognome) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
    }

    //secondo costruttore per la segreteria
    public Utente(String nome, String cognome) {
    }
}
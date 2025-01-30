public abstract class Utente {
    private int id;
    private String nome;
    private String cognome;

    // Costruttore
    public Utente(int id, String nome, String cognome) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
    }

    // Costruttore senza ID (utilizzato per l'inserimento nel database, id è
    // AUTOINCREMENT)
    public Utente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    // Getter e Setter per l'ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
}
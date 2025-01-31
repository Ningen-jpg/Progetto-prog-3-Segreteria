public class Studente extends Utente {
    private String matricola;


    public Studente(String nome, String cognome, String matricola) {
        super(nome, cognome);
        this.matricola = matricola;
    }

}

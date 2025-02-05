import java.util.Date;
import java.util.List;

//stiamo usando il factory method pattern
public class Studente extends Utente {
    private String matricola; // chiave primaria
    private Date dataNascita;
    private String residenza;
    private List<Esame> esami; // lista degli esami del corso di appartenenza (per i voti)
    private Corso pianoDiStudi; // indica il mio corso di laurea scelto
    private List<Esame> esamiSuperati;
    private List<Esame> esamiSostenuti; // in fase di valutazione
    private List<Esame> testCompletati;
    private boolean tasse;

    public Studente(String nome, String cognome, String matricola) {
        super(nome, cognome);
        this.matricola = matricola;

    }
}
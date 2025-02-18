import java.util.Date;
import java.util.List;

public class Studente extends Utente {
    private String matricola; // chiave primaria
    private Date dataNascita;
    private String residenza;
    private List<Esame> esami;
    private CorsoDiLaurea pianoDiStudi;
    private List<Esame> esamiSuperati;
    private List<Esame> esamiSostenuti;
    private List<Esame> testCompletati;
    private boolean tasse;
    private String password;

    public Studente(String nome, String cognome, String matricola) {
        super(nome, cognome);
        this.matricola = matricola;
    }

    // Getters e setters per tutti gli attributi

    public String getMatricola() {
        return matricola;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getResidenza() {
        return residenza;
    }

    public void setResidenza(String residenza) {
        this.residenza = residenza;
    }

    public List<Esame> getEsami() {
        return esami;
    }

    public void setEsami(List<Esame> esami) {
        this.esami = esami;
    }

    public CorsoDiLaurea getPianoDiStudi() {
        return pianoDiStudi;
    }

    public void setPianoDiStudi(CorsoDiLaurea pianoDiStudi) {
        this.pianoDiStudi = pianoDiStudi;
    }

    public List<Esame> getEsamiSuperati() {
        return esamiSuperati;
    }

    public void setEsamiSuperati(List<Esame> esamiSuperati) {
        this.esamiSuperati = esamiSuperati;
    }

    public List<Esame> getEsamiSostenuti() {
        return esamiSostenuti;
    }

    public void setEsamiSostenuti(List<Esame> esamiSostenuti) {
        this.esamiSostenuti = esamiSostenuti;
    }

    public List<Esame> getTestCompletati() {
        return testCompletati;
    }

    public void setTestCompletati(List<Esame> testCompletati) {
        this.testCompletati = testCompletati;
    }

    public boolean isTasse() {
        return tasse;
    }

    public void setTasse(boolean tasse) {
        this.tasse = tasse;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {}
}

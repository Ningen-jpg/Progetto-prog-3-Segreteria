import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Appello {
    private String idAppello;
    private Date dataAppello;
    private Docente docente;
    private List <Studente> listaStudenti;
    private Esame esameDiRiferimento;

    //costruttore momentaneo
    public Appello(String idAppello, Date dataAppello,Esame esameDiRiferimento) {
        this.idAppello = idAppello;
        this.dataAppello = dataAppello;
        this.esameDiRiferimento = esameDiRiferimento;
    }

    public Date getDataAppello() {
        return dataAppello;
    }

    public String getIdAppello() {
        return idAppello;
    }
    public Esame getEsameDiRiferimento() {
        return esameDiRiferimento;
    }
}

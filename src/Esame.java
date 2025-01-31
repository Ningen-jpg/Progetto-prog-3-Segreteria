import java.util.Date;

public class Esame {
    private Corso corso;
    private Date data;
    private String voto;

    public Esame(Corso corso, Date data, String voto) {
        this.corso = corso;
        this.data = data;
        this.voto = voto;
    }
}

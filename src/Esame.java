import java.util.Date;
import java.util.List;

public class Esame {
    private CorsoDiLaurea corso;
    private Date data;
    private String voto;
    private int CFU;
    private boolean conferma;
    private List <Appello> appelliEsame;

    public Esame(CorsoDiLaurea corso, Date data, String voto) {
        this.corso = corso;
        this.data = data;
        this.voto = voto;
    }
    public String getVoto(){
        return voto;
    }
    public CorsoDiLaurea getCorso(){
        return corso;
    }
    public Date getData(){
        return data;
    }
    public int getCFU(){
        return CFU;
    }
    public boolean getConferma(){
        return conferma;
    }
}

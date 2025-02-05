import java.util.List;
public class CorsoDiLaurea {
    int id;
    String nome;
    String descrizione;
    //lista degli esami presenti nel corso
    List <Esame> EsamiCorso;

    public CorsoDiLaurea(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }

}

@SuppressWarnings("ALL")

public class Notifica {
    private String esame;
    private String voto;
    private String data;

    public Notifica(String esame, String voto, String data) {
        this.esame = esame;
        this.voto = voto;
        this.data = data;
    }

    public String getEsame() {
        return esame;
    }

    public String getVoto() {
        return voto;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Hai ricevuto il voto per l'esame " + esame + " sostenuto il " + data + ": " + voto;
    }
}
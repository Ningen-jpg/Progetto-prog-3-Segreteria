public class Corso {
    private String nome;
    private int crediti;

    public Corso(String nome, int crediti) {
        this.nome = nome;
        this.crediti = crediti;
    }

    public String getNome() {
        return nome;
    }

    public int getCrediti() {
        return crediti;
    }
}

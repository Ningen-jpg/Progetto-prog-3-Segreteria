public interface Mediator {
    void inviaVoto(String destinatario, String nomeEsame, String messaggio); // Per il voto
    void inviaConferma(String destinatario, boolean conferma); // Per la conferma
}
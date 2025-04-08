import java.util.HashMap;
import java.util.Map;

public class NotificationService implements Mediator {
    private Map<String, Observer> partecipanti = new HashMap<>();

    //per aggiungere un nuovo osservatore
    public void registraPartecipante(String id, Observer observer) {
        partecipanti.put(id, observer);
    }

    //verrà richiamato dai Docenti
    @Override
    public void inviaVoto(String destinatario, String nomeEsame, String voto) {
        if (partecipanti.containsKey(destinatario)) {
            partecipanti.get(destinatario).update(destinatario, nomeEsame, voto, "data");
        } else {
            System.out.println("Errore: studente non trovato.");
        }
    }

    //verrà richiamato dagli studenti
    @Override
    public void inviaConferma(String destinatario, boolean conferma) {
        if (partecipanti.containsKey(destinatario)) {
            if(conferma) {
                System.out.println("Lo studente ha deciso di confermare il voto."); }
            else {
                System.out.println("Lo studente ha deciso di rifiutare il voto."); }
        } else {
            System.out.println("Errore: Docente non trovato.");
        }
    }
}

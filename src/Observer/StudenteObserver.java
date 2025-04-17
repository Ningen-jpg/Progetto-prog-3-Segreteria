package Observer;

import java.util.*;
@SuppressWarnings("ALL")
public class StudenteObserver implements Observer {

    private Map<String, List<Notifica>> notificaStudente = new HashMap<>();

    @Override
    public void update(String matricola, String esame, String voto, String data) {
        if (matricola == null || matricola.trim().isEmpty()) {
            System.out.println("ERRORE: Matricola non valida per la notifica");
            return;
        }

        // Verifica se esiste già una notifica identica
        List<Notifica> notificheEsistenti = notificaStudente.get(matricola);
        if (notificheEsistenti != null) {
            for (Notifica n : notificheEsistenti) {
                if (n.getEsame().equals(esame) && n.getVoto().equals(voto) && n.getData().equals(data)) {
                    System.out.println("DEBUG: Observer.Notifica duplicata rilevata per " + matricola + " - " + esame);
                    return;
                }
            }
        }

        Notifica nuovaNotifica = new Notifica(esame, voto, data);

        notificaStudente.putIfAbsent(matricola, new ArrayList<>());
        notificaStudente.get(matricola).add(nuovaNotifica);
    }

    public List<Notifica> getNotifiche(String matricola) {
        List<Notifica> notificheStudente = notificaStudente.get(matricola);
        return notificheStudente != null ? notificheStudente : new ArrayList<>();
    }
}
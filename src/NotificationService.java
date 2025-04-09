import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NotificationService implements Mediator {
    private Map<String, Observer> partecipanti = new HashMap<>();

    // per aggiungere un nuovo osservatore
    public void registraPartecipante(String id, Observer observer) {
        partecipanti.put(id, observer);
    }

    // verrà richiamato dai Docenti
    @Override
    public void inviaVoto(String matricola, String voto, String nomeEsame, String nomeDocente, String cognomeDocente) {

        if (partecipanti.containsKey(matricola)) {
            Observer studente = partecipanti.get(matricola);
            //da sistemare con l'aggiunta di abbellimenti per le stringhe
            studente.update(nomeEsame, nomeDocente, cognomeDocente, voto);
        } else {
            System.out.println("Errore: Studente non trovato.");
        }

    }
}

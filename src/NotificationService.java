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
    public void inviaVoto(DocenteSubject docenteSubject) {
        Scanner scanner = new Scanner(System.in);

        String matricola = scanner.nextLine();
        String voto = scanner.nextLine();
        String nomeEsame = scanner.nextLine();
        String nomeDocente = scanner.nextLine();
        String cognomeDocente = scanner.nextLine();

        if (partecipanti.containsKey(matricola)) {
            Observer studente = partecipanti.get(matricola);
            studente.update(nomeEsame, nomeDocente, cognomeDocente, voto);
        } else {
            System.out.println("Errore: Studente non trovato.");
        }
    }
}

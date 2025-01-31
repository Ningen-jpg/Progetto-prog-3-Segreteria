public class StudenteFactory implements UtenteFactory {
    @Override
    public Utente creaUtente(String nome, String cognome) {
        String matricola = null;
        return new Studente(nome, cognome, matricola);
    }
}

public class ConcreteUtenteFactory {
    //implementazione del factory method pattern
    public static Utente getUtente (String tipoUtente, String id,String password, String nome, String cognome)
    {
        if (tipoUtente == null)
        {
            return null;
        }

        switch (tipoUtente.toLowerCase()) {
            case "studente":
                return new Studente(id, password, nome, cognome);
            case "docente":
                return new Docente(id, password, nome, cognome); // Ritorna direttamente un Docente
            case "segreteria":
                return new Segreteria(id, password, nome, cognome);
            default:
                throw new IllegalArgumentException("Tipo di utente non valido");
        }
    }
}

/*andrebbe snellita nel caso volessimo rispettare solo i requisiti del progetto
poichè attualmente verrebbe usata solo per l'inserimento di un NUOVO "studente"
 */

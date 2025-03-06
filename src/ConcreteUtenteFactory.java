public class ConcreteUtenteFactory {
    //implementazione del factory method pattern
    public static Utente getUtente (String tipoUtente, String id,String password, String nome, String cognome)
    {
        if (tipoUtente == null)
        {
            return null;
        }

        return switch (tipoUtente.toLowerCase()) {
            case "studente" -> new Studente(id, password, nome, cognome);
            case "docente" -> new Docente(id, password, nome, cognome);
            case "segreteria" -> new Segreteria(id, password, nome, cognome);
            default -> null;
        };
    }
}

/*andrebbe snellita nel caso volessimo rispettare solo i requisiti del progetto
poichè attualmente verrebbe usata solo per l'inserimento di un NUOVO "studente"
 */

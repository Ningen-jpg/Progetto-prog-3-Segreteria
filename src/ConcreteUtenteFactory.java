public class ConcreteUtenteFactory {
    //implementazione del factory method pattern
    public static Utente getUtente (String tipoUtente, String matricola, String nome, String cognome)
    {
        if (tipoUtente == null)
        {
            return null;
        }

        switch(tipoUtente.toLowerCase())
        {
            case "studente":
                return new Studente(matricola, nome, cognome);
            case "docente":
                return new Docente(matricola, nome, cognome);
            case "segreteria":
                return new Segreteria(matricola, nome, cognome);
            default:
                return null;

        }
    }
}

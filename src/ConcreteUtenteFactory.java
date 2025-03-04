public class ConcreteUtenteFactory {
    //implementazione del factory method pattern
    public static Utente getUtente (String tipoUtente, String id,String password, String nome, String cognome)
    {
        if (tipoUtente == null)
        {
            return null;
        }

        switch(tipoUtente.toLowerCase())
        {
            case "studente":
                return new Studente(id,password, nome, cognome);
            case "docente":
                return new Docente(id,password, nome, cognome);
            case "segreteria":
                return new Segreteria(id, password, nome, cognome);
            default:
                return null;

        }
    }
}

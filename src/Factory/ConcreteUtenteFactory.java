package Factory;

import Model.Docente;
import Model.Segreteria;
import Model.Studente;
import Model.Utente;

@SuppressWarnings("ALL")
//implementazione del factory method pattern

public class ConcreteUtenteFactory {
    public static Utente getUtente (String tipoUtente, String id, String password, String nome, String cognome)
    {
        if (tipoUtente == null)
        {
            return null;
        }

        switch (tipoUtente.toLowerCase()) {
            case "studente":
                return new Studente(id, password, nome, cognome);
            case "docente":
                return new Docente(id, password, nome, cognome);
            case "segreteria":
                return new Segreteria(id, password, nome, cognome);
            default:
                throw new IllegalArgumentException("Tipo di utente non valido");
        }
    }
}
package Mediator;
import Observer.DocenteSubject;
/*
    Interfaccia per il mediator
 */
@SuppressWarnings("ALL")

public interface Mediator {
    void inviaVoto(DocenteSubject docenteSubject, String docenteID);
    void gestisciNotifica(String matricola);
}
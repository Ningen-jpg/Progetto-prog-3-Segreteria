package Observer;

@SuppressWarnings("ALL")

public interface Subject {
    void notifyObservers(String matricola, String esame, String voto);
}
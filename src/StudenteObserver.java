class StudenteObserver implements Observer {
    private String matricola;

    public StudenteObserver(String matricola) {
        this.matricola = matricola;
    }

    @Override
    public void update(String mat, String nome, int voto) {
        System.out.println("🔔 Notifica: Il docente ha inserito il voto " + voto + " per l'esame di " + nome);
        Mediator.getInstance().riceviVoto(matricola, nome, voto);
    }
}
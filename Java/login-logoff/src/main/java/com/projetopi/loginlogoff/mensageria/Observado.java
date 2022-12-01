package com.projetopi.loginlogoff.mensageria;

public interface Observado {
    void adicionaObservador(Observer observer);
    void removeObservador(Observer observer);
    void notificaObservadores();
}

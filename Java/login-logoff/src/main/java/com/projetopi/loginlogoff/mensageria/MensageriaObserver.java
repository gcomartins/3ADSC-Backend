package com.projetopi.loginlogoff.mensageria;

public class MensageriaObserver implements Observer{

    @Override
    public void atualizar(Double valorInicialObjetivo, Double valorFinalObjetivo) {
        if (valorInicialObjetivo >= valorFinalObjetivo){
            System.out.println("Parabéns! Você atingiu seu objetivo!");
        }
    }
}

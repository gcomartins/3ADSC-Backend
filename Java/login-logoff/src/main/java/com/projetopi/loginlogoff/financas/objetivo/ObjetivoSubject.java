package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.FilaObj;
import com.projetopi.loginlogoff.mensageria.MensageriaObserver;
import com.projetopi.loginlogoff.mensageria.Observado;
import com.projetopi.loginlogoff.mensageria.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ObjetivoSubject implements Observado {

    private Double valorInicialObjetivo;
    private Double valorFinalObjetivo;
//    private List<Observer> observerList = new ArrayList<>();

    FilaObj<Observer> filaObserver = new FilaObj(30);
    public ObjetivoSubject(Double valorInicialObjetivo, Double valorFinalObjetivo) {
        this.valorInicialObjetivo = valorInicialObjetivo;
        this.valorFinalObjetivo = valorFinalObjetivo;
    }

    public ObjetivoSubject() {
    }

    public Double getValorInicialObjetivo() {
        return valorInicialObjetivo;
    }

    public void setValorInicialObjetivo(Double valorInicialObjetivo) {
        this.valorInicialObjetivo = valorInicialObjetivo;
    }

    public Double getValorFinalObjetivo() {
        return valorFinalObjetivo;
    }

    public void setValorFinalObjetivo(Double valorFinalObjetivo) {
        this.valorFinalObjetivo = valorFinalObjetivo;
    }

    @Override
    public void adicionaObservador(Observer observer) {
        filaObserver.insert(observer);
    }

    @Override
    public void removeObservador(Observer observer) {
        filaObserver.poll();
    }

    @Override
    public void notificaObservadores() {
//        MensageriaObserver observer = new MensageriaObserver();
//        adicionaObservador(observer);
        while (!filaObserver.isEmpty()){
            filaObserver.poll().atualizar(this.valorInicialObjetivo, this.valorFinalObjetivo);
        }

    }
}

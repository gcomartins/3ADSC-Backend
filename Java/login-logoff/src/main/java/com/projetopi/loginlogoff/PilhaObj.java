package com.projetopi.loginlogoff;

public class PilhaObj<T> {

    // 01) Atributos
    private T pilha[];
    private int topo;

    // 02) Construtor
    public PilhaObj(int capacidade) {
        this.pilha = (T[]) new Object[capacidade];
        topo = -1;
    }

    // 03) Método isEmpty
    public Boolean isEmpty() {
        return this.topo < 0;

    }

    // 04) Método isFull
    public Boolean isFull() {
        //n-1
        return this.topo >= this.pilha.length - 1;

    }

    // 05) Método push
    public void push(T info) {
        if (isFull()) {
            throw new IllegalStateException("Pilha cheia");
        }
        this.pilha[++topo] = info;
    }

    // 06) Método pop
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        return this.pilha[this.topo--];
    }

    // 07) Método peek
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return this.pilha[this.topo];
    }

    // 08) Método exibe
    public void exibe() {
        if (isEmpty()) {
            System.out.println("Pilha vazia");
        } else {
            for (int i = this.topo; i >= 0; i--) {
                System.out.println(pilha[i]);
            }
        }
    }

    public boolean ehPalindromo() {
        T auxiliarPrimeiraMetade[] = (T[]) new Object[(topo + 1) / 2];
        T auxiliarSegundaMetade[] = (T[]) new Object[(topo + 1) / 2];
        int contadorSegundaMetade = (topo + 1) / 2;

        if ((this.topo + 1) % 2 == 0) {
            for (int i = 0; i < this.topo + 1; i++) {
                if (i < (this.topo + 1) / 2) {
                    auxiliarPrimeiraMetade[i] = pilha[i];
                } else {
                    contadorSegundaMetade--;
                    auxiliarSegundaMetade[contadorSegundaMetade] = pilha[i];
                }
            }
            for (int i = 0; i < auxiliarPrimeiraMetade.length; i++) {
                if (auxiliarPrimeiraMetade[i] != auxiliarSegundaMetade[i]) {
                    return false;
                }
            }
            return true;
        } else {
            for (int i = 0; i < this.topo + 1; i++) {
                if (i < (this.topo + 1) / 2) {
                    auxiliarPrimeiraMetade[i] = pilha[i];
                } else {
                    if (!((topo + 1) / 2 == i)) {
                        contadorSegundaMetade--;
                        auxiliarSegundaMetade[contadorSegundaMetade] = pilha[i];
                    }
                }
            }
            for (int i = 0; i < auxiliarPrimeiraMetade.length; i++) {
                if (auxiliarPrimeiraMetade[i] != auxiliarSegundaMetade[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    //Getters & Setters (manter)
    public int getTopo() {
        return topo;
    }
}

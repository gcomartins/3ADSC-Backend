package com.projetopi.loginlogoff.financas.receita;

import com.projetopi.loginlogoff.financas.Financas;
import com.projetopi.loginlogoff.usuario.Usuario;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
public class Receita extends Financas {

    private boolean isRecorrente;
    @Min(1)
    @Max(31)
    private int frequencia;

    public Receita(int codigo, String nome, String descricao, double valor, Date data,
                   String categoria, boolean isRecorrente, int frequencia) {
        super(codigo, nome, descricao, valor, data, categoria);
        this.isRecorrente = isRecorrente;
        this.frequencia = frequencia;
    }

    public Receita() {
    }

    public boolean isRecorrente() {
        return isRecorrente;
    }

    public void setRecorrente(boolean recorrente) {
        isRecorrente = recorrente;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    @Override
    public void modificaSaldo(Usuario usuario) {
        usuario.setSaldo(usuario.pegueSaldo() + getValor());
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Receita ----------" +
                        "\nisRecorrente: %s " +
                        "\nFrequência: %d",isRecorrente, frequencia);
    }
}

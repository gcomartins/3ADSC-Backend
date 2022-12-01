package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.financas.Financas;

import javax.persistence.Entity;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
@Entity
public class Objetivo extends Financas {
    @DecimalMin("0.1")
    @DecimalMax("100000000")
    private double valorAtual;
    @FutureOrPresent
    private LocalDate dataFinal;

    public Objetivo(int codigo, String nome, String descricao, double valor,
                    LocalDate data, String categoria, double valorAtual, LocalDate dataFinal) {
        super(codigo, nome, descricao, valor, data, categoria);
        this.valorAtual = valorAtual;
        this.dataFinal = dataFinal;
    }
    public Objetivo( String nome, String descricao, double valor,
                    LocalDate data, String categoria, double valorAtual, LocalDate dataFinal) {
        super(nome, descricao, valor, data, categoria);
        this.valorAtual = valorAtual;
        this.dataFinal = dataFinal;
    }


    public Objetivo() {
        super();
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }



//    @Override
//    public String toString() {
//        return String.format
//                ("\n---------- Objetivo ----------" +
//                        "\nValor Atual: %.2f" +
//                        "\nData Final: %s", valorAtual, dataFinal);
//    }
}

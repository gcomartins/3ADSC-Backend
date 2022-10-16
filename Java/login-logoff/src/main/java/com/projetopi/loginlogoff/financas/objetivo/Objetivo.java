package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.financas.Financas;
import com.projetopi.loginlogoff.usuario.Usuario;

import javax.persistence.Entity;
import javax.validation.constraints.*;
import java.util.Date;
@Entity
public class Objetivo extends Financas {
    @DecimalMin("0.1")
    @DecimalMax("100000000")
    private double valorAtual;
    @FutureOrPresent
    private Date dataFinal;

    public Objetivo(int codigo, String nome, String descricao, double valor,
                    Date data, String categoria, double valorAtual, Date dataFinal) {
        super(codigo, nome, descricao, valor, data, categoria);
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

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    @Override
    public void modificaSaldo(Usuario usuario) {
        usuario.setSaldo(usuario.pegueSaldo() + valorAtual);
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Objetivo ----------" +
                        "\nValor Atual: %.2f" +
                        "\nData Final: %s", valorAtual, dataFinal);
    }
}

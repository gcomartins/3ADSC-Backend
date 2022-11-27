package com.projetopi.loginlogoff.financas;

import java.time.LocalDate;
import java.time.Month;

public class RelatorioMensal {
    private Double valor;
    private Integer mes;
    private Integer ano;

    public RelatorioMensal(Double valor, Integer mes, int ano) {
        this.valor = valor;
        this.mes = mes;
        this.ano = ano;
    }


    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return "RelatorioMensal{" +
                "valor=" + valor +
                ", mes=" + mes +
                ", ano=" + ano +
                '}';
    }
}

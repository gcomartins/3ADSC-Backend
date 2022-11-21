package com.projetopi.loginlogoff.financas.despesa;
import com.projetopi.loginlogoff.financas.Financas;
import com.projetopi.loginlogoff.usuario.Usuario;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Date;
@Entity
public class Despesa extends Financas {

    private boolean isPago;
    @Min(1)
    @Max(480)
    private int qtdParcelas;

    public Despesa() {
    }

    public Despesa(int codigo, String nome, String descricao, double valor, LocalDate data,
                   String categoria, boolean isPago, int qtdParcelas) {
        super(codigo, nome, descricao, valor, data, categoria);
        this.isPago = isPago;
        this.qtdParcelas = qtdParcelas;
    }
    public Despesa(String nome, String descricao, double valor, LocalDate data,
                   String categoria, boolean isPago, int qtdParcelas) {
        super( nome, descricao, valor, data, categoria);
        this.isPago = isPago;
        this.qtdParcelas = qtdParcelas;
    }

    public boolean isPago() {
        return isPago;
    }

    public void setPago(boolean pago) {
        isPago = pago;
    }

    public int getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(int qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Despesa ---------- " +
                        "\nisPago: %s" +
                        "\nQuantidade de Parcelas: %d", isPago, qtdParcelas);
    }
}
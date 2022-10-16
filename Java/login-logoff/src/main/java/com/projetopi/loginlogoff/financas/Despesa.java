package com.projetopi.loginlogoff.financas;
import com.projetopi.loginlogoff.usuario.Usuario;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
@Entity
public class Despesa extends Financas {

    private boolean isPago;
    @Min(1)
    @Max(480)
    private int qtdParcelas;

    public Despesa() {
    }

    public Despesa(int codigo, String nome, String descricao, double valor, Date data,
                   String categoria, boolean isPago, int qtdParcelas) {
        super(codigo, nome, descricao, valor, data, categoria);
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
    public void modificaSaldo(Usuario usuario) {
        usuario.setSaldo(usuario.pegueSaldo() - getValor());
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Despesa ---------- " +
                        "\nisPago: %s" +
                        "\nQuantidade de Parcelas: %d", isPago, qtdParcelas);
    }
}
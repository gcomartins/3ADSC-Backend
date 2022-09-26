package com.projetopi.loginlogoff;
import javax.persistence.Entity;
import java.util.Date;
@Entity
public class Despesa extends Financas{

    private boolean isPago;
    private int qtdParcelas;

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
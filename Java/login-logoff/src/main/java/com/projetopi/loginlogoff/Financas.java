package com.projetopi.loginlogoff;

import javax.persistence.*;
import java.util.Date;
//Essa annotation faz com que essa tabela não seja persistida no jpa, porem suas classes filhas
//que possuirem annotarion @Entity serão criadas
@MappedSuperclass
public abstract class Financas {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int codigo;
    private String nome;
    private String descricao;
    private double valor;
    private Date data;
    private String categoria;
    //ID_USUARIO é o nome da fk
    @ManyToOne
    @JoinColumn(name = "FK_USUARIO")
    private Usuario usuario;
    public Financas(int codigo, String nome, String descricao, double valor, Date data, String categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

    public abstract void modificaSaldo(Usuario usuario);

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Finanças ----------" +
                        "\nCódigo: %d" +
                        "\nNome: %s" +
                        "\nDescrição: %s" +
                        "\nValor: %.2f" +
                        "\nData: %s" + data +
                        "\nCategoria: %s", codigo, nome, descricao, valor, data, categoria);
    }
}
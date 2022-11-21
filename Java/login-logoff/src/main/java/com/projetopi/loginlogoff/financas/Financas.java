package com.projetopi.loginlogoff.financas;

import com.projetopi.loginlogoff.usuario.Usuario;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
//Essa annotation faz com que essa tabela não seja persistida no jpa, porem suas classes filhas
//que possuirem annotarion @Entity serão criadas
@MappedSuperclass
public abstract class Financas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;
    @Size(min = 3,max = 50)
    private String nome;
    @Size(min = 3,max = 100)
    private String descricao;
    @DecimalMin("0.1")
    @DecimalMax("100000000")
    private double valor;
    @FutureOrPresent
    private LocalDate data;
    @Size(min = 3, max = 20)
    private String categoria;
    //ID_USUARIO é o nome da fk
    @ManyToOne
    private Usuario usuario;
    public Financas() {
    }

    public Financas(int codigo, String nome, String descricao, double valor, LocalDate data, String categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

    public Financas(String nome, String descricao, double valor, LocalDate data, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getFkUsuario(){
        return this.usuario.getIdUsuario();
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return String.format
                ("\n---------- Informações ----------" +
                        "\nCódigo: %d" +
                        "\nNome: %s" +
                        "\nDescrição: %s" +
                        "\nValor: %.2f" +
                        "\nData: %s" + data +
                        "\nCategoria: %s", codigo, nome, descricao, valor, data, categoria);
    }
}
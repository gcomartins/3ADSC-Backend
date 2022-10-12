package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.financas.Despesa;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class Usuario {

    @Id // PK - chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private double saldo;
    private String senha;
    private Date dataNascimento;
    private Boolean isAutenticado;
    //Indica que a classe Usuario(One) possuira varios objetivos(Many)
    @OneToMany(mappedBy = "usuario")
    private List<Objetivo> objetivos;
    @OneToMany(mappedBy = "usuario")
    private List<Despesa> despesas;
    @OneToMany(mappedBy = "usuario")
    private List<Receita> receitas;


    public Usuario(String nome, String email, String cpf,
                   double saldo, String senha, Date dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.saldo = saldo;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public Usuario() {

    }

    public Integer pegueIdUsuario() {
        return id;
    }

    public void setIdUsuario(int idUsuario) {
        this.id = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double pegueSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String pegueSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setAutenticado(Boolean autenticado) {
        isAutenticado = autenticado;
    }

    public Boolean pegueIsAutenticado() {
        return isAutenticado;
    }



    @Override
    public String toString() {
        return String.format
                ("\n---------- Usuario ----------" +
                        "\nidUsuario: %d" +
                        "\nNome: %s " +
                        "\nEmail: %s " +
                        "\nCPF: %s " +
                        "\nSaldo: %.2f " +
                        "\nSenha: *** " +
                        "\nData de Nascimento: %s", id, nome, email, cpf, saldo, dataNascimento);
    }
}

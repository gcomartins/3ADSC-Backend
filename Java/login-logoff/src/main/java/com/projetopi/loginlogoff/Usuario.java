package com.projetopi.loginlogoff;

import java.sql.Date;

public class Usuario {
    private String nome;
    private Date dataDeNascimento;
    private String cpf;
    private String email;
    private String senha;

    public Usuario(String nome, Date dataDeNascimento, String cpf, String email, String senha) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return String.format("Usuario : %s \n" +
                "Data de nascimento: %s " +
                "\n cpf = %s \n" +
                "email = %s \n" +
                "senha = %s",getNome(),getDataDeNascimento(),getCpf(),getEmail(),getSenha());
    }
}

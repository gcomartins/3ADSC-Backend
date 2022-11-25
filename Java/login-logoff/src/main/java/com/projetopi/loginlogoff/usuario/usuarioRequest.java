package com.projetopi.loginlogoff.usuario;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class usuarioRequest {
    @Id // PK - chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;
    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;
    @Email
    private String email;
    @NotBlank
    @Size(min = 3, max = 100)
    private String senha;
    @PastOrPresent
    private String dataNascimento;
    private Boolean isAutenticado;

    public usuarioRequest(Integer id, String nome, String email, String senha, String dataNascimento, Boolean isAutenticado) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.isAutenticado = isAutenticado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getAutenticado() {
        return isAutenticado;
    }

    public void setAutenticado(Boolean autenticado) {
        isAutenticado = autenticado;
    }
}

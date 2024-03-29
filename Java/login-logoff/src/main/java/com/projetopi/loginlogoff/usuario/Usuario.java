package com.projetopi.loginlogoff.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public class Usuario {

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
    private LocalDate dataNascimento;
    private Boolean isAutenticado;
    @JsonIgnore
    private byte[] arquivoTxt;


    public Usuario(String nome, String email,
                    String senha, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public Usuario() {

    }

    public Integer getIdUsuario() {
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


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setAutenticado(Boolean autenticado) {
        isAutenticado = autenticado;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getAutenticado() {
        return isAutenticado;
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
                        "\nSenha: *** " +
                        "\nData de Nascimento: %s", id, nome, email, dataNascimento);
    }
}

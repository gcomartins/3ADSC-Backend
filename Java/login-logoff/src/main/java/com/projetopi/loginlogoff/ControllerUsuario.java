package com.projetopi.loginlogoff;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {
    List<Usuario> usuarios = new ArrayList<>();

    LocalDateTime hoje = LocalDateTime.now();

    @PostMapping
    public Usuario cadastrarUsuario(@RequestBody Usuario u) {
        LocalDateTime dataDeNascimento = LocalDateTime.of
                ((u.getDataNascimento().getYear() + 1900), u.getDataNascimento().getMonth() + 1,
                        u.getDataNascimento().getDate() + 1, 0, 0, 0);
        long idade = dataDeNascimento.until(hoje, ChronoUnit.YEARS);

        if (u.getEmail().contains("@") && u.pegueSenha().length() > 8 && u.getCpf().length() == 11
                && idade > 15) {
            usuarios.add(u);
            return u;
        }
        return null;
    }

    @GetMapping
    public List getUsuario( ) {
            return usuarios;
        }


    @PostMapping ("autenticar/{cpf}/{senha}")
    public String autenticarUsuario( @PathVariable String cpf, @PathVariable String senha) {
        for (Usuario u : usuarios) {
            if (u.getCpf().toString().equals(cpf) && u.pegueSenha().equals(senha)) {
                u.setAutenticado(true);
                return String.format("Login encontrado\n Bem vindo " + u.getNome());
            }
        }

        return null;
    }
}



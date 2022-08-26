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
                ((u.getDataDeNascimento().getYear() + 1900), u.getDataDeNascimento().getMonth() + 1,
                        u.getDataDeNascimento().getDate() + 1, 0, 0, 0);
        long idade = dataDeNascimento.until(hoje, ChronoUnit.YEARS);
        if (!u.getEmail().contains("@")) System.out.println("Email invalido");
        else if (u.getSenha().length() < 8 || u.getSenha().matches("!@#$%&*()"))
            System.out.println("senha invalida");
        else if (u.getCpf().length() != 11) System.out.println("cpf invalido");
        else if (idade < 15) System.out.println("idade Invalida");
        else {
            usuarios.add(u);
            return u;
        }
        return null;
    }

    @GetMapping
    public Usuario getUsuario(@RequestBody Usuario a) {
        for (Usuario u : usuarios) {
            if (u.getCpf().toString().contains(a.getCpf()) && u.getSenha().equals(a.getSenha())) {
                return u;
                }
            }

            return null;
        }
    }


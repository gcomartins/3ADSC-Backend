package com.projetopi.loginlogoff;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario u) {
        LocalDateTime dataDeNascimento = LocalDateTime.of
                ((u.getDataNascimento().getYear() + 1900), u.getDataNascimento().getMonth() + 1,
                        u.getDataNascimento().getDate() + 1, 0, 0, 0);
        long idade = dataDeNascimento.until(hoje, ChronoUnit.YEARS);

        if (u.getEmail().contains("@") && u.pegueSenha().length() > 8 && u.getCpf().length() == 11
                && idade > 15) {
            usuarios.add(u);
            return ResponseEntity.status(201).body(u);
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping
    public ResponseEntity<List> getUsuario() {
            return ResponseEntity.status(202).body(usuarios);
        }


    @PostMapping ("autenticar/{cpf}/{senha}")
    public ResponseEntity<String> autenticarUsuario( @PathVariable String cpf, @PathVariable String senha) {
        for (Usuario u : usuarios) {
            if (u.getCpf().toString().equals(cpf) && u.pegueSenha().equals(senha)) {
                u.setAutenticado(true);
                return ResponseEntity.status(201).body(String.format("Login realizado com ssucesso "));
            }
        }

        return  ResponseEntity.status(201).body(String.format("Cpf e/ou senha incorretos"));
    }
}



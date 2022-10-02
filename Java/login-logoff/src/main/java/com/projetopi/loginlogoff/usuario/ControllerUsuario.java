package com.projetopi.loginlogoff.usuario;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.DecimalMax;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuarioNovo) {
        usuarioNovo.setAutenticado(false);
        Usuario usuarioCadastrado = this.usuarioRepository.save(usuarioNovo);
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuario() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.status(200).body(usuarios);
    }


    @PutMapping("autenticar/{cpf}/{senha}")
    public ResponseEntity<String> autenticarUsuario(@PathVariable String cpf, @PathVariable String senha) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.pegueSenha().equals(senha)) {
                u.setAutenticado(true);
                this.usuarioRepository.save(u);
                return ResponseEntity.status(201).body(String.format("Login realizado com sucesso \n " + u.toString()));
            }
        }

        return ResponseEntity.status(201).body(String.format("Cpf e/ou senha incorretos"));
    }

    @PutMapping("senha/{cpf}/{senha}/{senhaNova}")
    public ResponseEntity<String> atualizarSenha(@PathVariable String cpf, @PathVariable String senha,
                                                 @PathVariable String senhaNova) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.pegueSenha().equals(senha)) {
                if (senhaNova.length() >= 8) u.setSenha(senhaNova);
                this.usuarioRepository.save(u);
                return ResponseEntity.status(201).body(String.format("Senha Atualizada \n " + u.toString()));
            }
        }
        return ResponseEntity.status(400).body(String.format("Senha e/ou cpf atuais incorretos \n"));
    }

    @PutMapping("/nome/{cpf}/{senha}/{nome}")
    public ResponseEntity<String> atualizarNome(@PathVariable String cpf, @PathVariable String senha,
                                                @PathVariable String nome) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.pegueSenha().equals(senha)) {
                if (!nome.equals(u.getNome())) {
                    u.setNome(nome);
                    usuarioRepository.save(u);
                    return ResponseEntity.status(201).body(String.format("Nome atualizado \n " + u.toString()));
                } else return ResponseEntity.status(400).body(String.format("Você está utilizando esta nome  \n "));
            }
        }
        return ResponseEntity.status(400).body(String.format("Senha e/ou cpf atuais incorretos \n"));
    }

    @PutMapping("email/{cpf}/{senha}/{email}")
    public ResponseEntity<String> atualizarEmail(@PathVariable String cpf, @PathVariable String senha,
                                                 @PathVariable String email) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        Boolean isAtualizado = false;
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.pegueSenha().equals(senha)) {
                if (!email.equals(u.getEmail())) {
                    u.setEmail(email);
                    isAtualizado = true;
                    usuarioRepository.save(u);
                    return ResponseEntity.status(201).body(String.format("Email  atualizado \n " + u.toString()));
                } else return ResponseEntity.status(400).body(String.format("Você está utilizando esse email\n "));
            }
        }
        return ResponseEntity.status(400).body(String.format("Senha e/ou cpf atuais incorretos \n"));
    }
    @DeleteMapping("{cpf}/{senha}")
    public ResponseEntity<String> atualizarEmail(@PathVariable String cpf, @PathVariable String senha) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.pegueSenha().equals(senha)) {
            int id = u.pegueIdUsuario();
            usuarioRepository.delete(u);
            return ResponseEntity.status(200).body(String.format("Usuario removido \n " + u.toString()));
            }
        }

            return ResponseEntity.status(404).body(String.format("Informações incorretas \n "));
    }
}



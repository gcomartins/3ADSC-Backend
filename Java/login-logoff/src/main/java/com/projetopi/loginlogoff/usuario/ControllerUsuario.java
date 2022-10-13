package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.objetivo.ObjetivoRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ObjetivoRepository objetivoRepository;

    @PostMapping
    public ResponseEntity<Usuario> cadastrar( @Valid  @RequestBody Usuario usuarioNovo) {

        usuarioNovo.setAutenticado(false);
        Usuario usuarioCadastrado = this.usuarioRepository.save(usuarioNovo);
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.status(200).body(usuarios);
    }




    @PutMapping("login/{cpf}/{senha}")
    public ResponseEntity<Usuario> login(@PathVariable String cpf, @PathVariable String senha) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha)) {
                u.setAutenticado(true);
                this.usuarioRepository.save(u);
                return ResponseEntity.status(202).body(u);
            }
        }

        return ResponseEntity.status(404).build();
    }

    @PutMapping("logoff/{cpf}/{senha}")
    public ResponseEntity<Usuario> logoff(@PathVariable String cpf, @PathVariable String senha) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha)) {
                u.setAutenticado(false);
                this.usuarioRepository.save(u);
                return ResponseEntity.status(200).body(u);
            }
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("senha/{cpf}/{senha}/{senhaNova}")
    public ResponseEntity<Usuario> atualizarSenha(@PathVariable String cpf, @PathVariable String senha,
                                                 @PathVariable String senhaNova) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha)) {
                if (senhaNova.length() >= 8) u.setSenha(senhaNova);
                this.usuarioRepository.save(u);
                return ResponseEntity.status(200).body(u);
            }
        }
        return ResponseEntity.status(400).build();
    }

    @PutMapping("/nome/{cpf}/{senha}/{nome}")
    public ResponseEntity<Usuario> atualizarNome(@PathVariable String cpf,  @PathVariable String senha,
                                                @PathVariable String nome) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha)) {
                if (!nome.equals(u.getNome())) {
                    u.setNome(nome);
                    usuarioRepository.save(u);
                    return ResponseEntity.status(200).body(u);
                } else return ResponseEntity.status(400).build();
            }
        }
        return ResponseEntity.status(400).build();
    }

    @PutMapping("email/{cpf}/{senha}/{email}")
    public ResponseEntity<Usuario> atualizarEmail(@PathVariable String cpf, @PathVariable String senha,
                                                 @PathVariable String email) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        Boolean isAtualizado = false;
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha)) {
                if (!email.equals(u.getEmail())) {
                    u.setEmail(email);
                    isAtualizado = true;
                    usuarioRepository.save(u);
                    return ResponseEntity.status(200).body(u);
                } else return ResponseEntity.status(400).build();
            }
        }
        return ResponseEntity.status(400).build();
    }
    @DeleteMapping("{cpf}/{senha}")
    public ResponseEntity<Usuario> atualizarEmail(@PathVariable String cpf, @PathVariable String senha) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha)) {
            int id = u.getIdUsuario();
            usuarioRepository.delete(u);
            return ResponseEntity.status(200).body(u);
            }
        }

            return ResponseEntity.status(404).build();
    }
    @PostMapping("/gerarCsv/{idUsuario}/{nomeArquivo}")
    public ResponseEntity<String> gerarCsv(@PathVariable Integer idUsuario,@PathVariable  String nomeArquivo){
        List<Objetivo> objetivos = objetivoRepository.findAll();
        int qtdObj = objetivos.size();
        ListaObj listaObjetos = new ListaObj<>(qtdObj);
        for (Objetivo objetivoAtual : objetivos){
            listaObjetos.adiciona(objetivoAtual);
        }
        listaObjetos.gravaArquivoCsvObjetivo(listaObjetos, nomeArquivo);
        return ResponseEntity.status(200).body(listaObjetos.gravaArquivoCsvObjetivo(listaObjetos, nomeArquivo));
    }
}



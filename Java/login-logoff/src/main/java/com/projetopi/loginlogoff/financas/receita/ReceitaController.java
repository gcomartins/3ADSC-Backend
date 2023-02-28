package com.projetopi.loginlogoff.financas.receita;

import com.projetopi.loginlogoff.Log;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    @Autowired
    ReceitaRepository receitaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private ReceitaService receitaService;
    Log log = new Log();

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Receita>> listarTodasReceitasDoUsuario(@PathVariable int idUsuario) {
        List<Receita> resposta = receitaService.listarTodasReceitasDoUsuario(idUsuario);
        if (resposta != null) {
            return resposta.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.status(200).body(resposta);

        }
        return ResponseEntity.status(404).build();

    }

    @GetMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity <Receita> buscarReceitaDoUsuario(@PathVariable int idUsuario, @PathVariable int idReceita) {
        Optional<Receita> resposta = receitaService.buscarReceitaDoUsuario(idUsuario, idReceita);
        if (resposta != null) {
            return resposta.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.status(200).body(resposta.get());
        }
        return ResponseEntity.status(404).build();
    }


    @PostMapping("/{idUsuario}")
    public ResponseEntity<Receita> criarReceita(@Valid @PathVariable int idUsuario, @RequestBody Receita receita) {
        Receita resposta = receitaService.criarReceita(idUsuario, receita);
        return resposta != null ? ResponseEntity.status(201).body(receita) : ResponseEntity.status(400).build();
    }

    @PutMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity<Receita> atualizarReceita(@Valid @PathVariable int idUsuario, @PathVariable int idReceita,
                                                    @RequestBody Receita receita) {
        Receita resposta = receitaService.atualizarReceita(idUsuario, idReceita, receita);
        if (resposta != null) return ResponseEntity.status(200).body(resposta);
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity<Receita>  deletarReceita(@PathVariable int idUsuario, @PathVariable int idReceita) {
        Optional<Receita> resposta = receitaService.deletarReceita(idUsuario, idReceita);
        return resposta != null ? ResponseEntity.status(200).body(resposta.get()) : ResponseEntity.status(404).build();
    }

    @DeleteMapping("/deletarTodas/{idUsuario}")
    public ResponseEntity<List<Receita>> deletarTodasReceitas(@PathVariable int idUsuario) {
        List<Receita> resposta = receitaService.deletarTodasReceitas(idUsuario);
        if (resposta != null) return resposta.isEmpty() ? ResponseEntity.status(202).build() :
                ResponseEntity.status(200).body(resposta);
        else return ResponseEntity.status(404).build();
    }

}

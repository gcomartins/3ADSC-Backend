package com.projetopi.loginlogoff.financas.receita;

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

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    @Autowired
    ReceitaRepository receitaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Receita>> listarTodasReceitasDoUsuario(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            List<Receita> todasReceitas = receitaRepository.findAll();
            List<Receita> todasReceitasDoUsuario = new ArrayList<>();
            for (Receita receitaAtual : todasReceitas) {
                if (receitaAtual.getFkUsuario() == idUsuario) {
                    todasReceitasDoUsuario.add(receitaAtual);
                }
            }
            if (todasReceitasDoUsuario.isEmpty()) {
                return ResponseEntity.status(204).build();
            }
            return ResponseEntity.status(200).body(todasReceitasDoUsuario);
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity<Receita> listarTodasReceitasDoUsuario(@PathVariable int idUsuario, @PathVariable int idReceita) {
        if (usuarioRepository.existsById(idUsuario) && receitaRepository.existsById(idReceita)) {
            List<Receita> todasReceitas = receitaRepository.findAll();
            for (Receita receitaAtual : todasReceitas) {
                if (receitaAtual.getFkUsuario() == idUsuario && receitaAtual.getCodigo() == idReceita) {
                    return ResponseEntity.status(200).body(receitaAtual);
                }
            }
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Receita> criarReceita(@Valid @PathVariable int idUsuario, @RequestBody Receita receita) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            receita.setUsuario(usuario.get());
            return ResponseEntity.status(201).body(receitaRepository.save(receita));
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity<Receita> atualizarReceita(@Valid @PathVariable int idUsuario, @PathVariable int idReceita,
                                                      @RequestBody Receita receita) {
        if (usuarioRepository.existsById(idUsuario) && receitaRepository.existsById(idReceita)) {
            Receita receitaAtualizada = new Receita(
                    idReceita,
                    receita.getNome(),
                    receita.getDescricao(),
                    receita.getValor(),
                    receita.getData(),
                    receita.getCategoria(),
                    receita.isRecorrente(),
                    receita.getFrequencia()
            );
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            receitaAtualizada.setUsuario(usuario.get());
            return ResponseEntity.status(200).body(receitaRepository.save(receitaAtualizada));
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity<Receita> deletarReceita(@PathVariable int idUsuario, @PathVariable int idReceita) {
        if (usuarioRepository.existsById(idUsuario) && receitaRepository.existsById(idReceita)) {
            Optional<Receita> receita = receitaRepository.findById(idReceita);
            receitaRepository.deleteById(idReceita);
            return ResponseEntity.status(200).body(receita.get());
        }
        return ResponseEntity.status(404).build();
    }
    @DeleteMapping("/deletarTodas/{idReceita}")
    public ResponseEntity<List<Receita>> deletarTodasReceitas(@PathVariable int idReceita) {
        if (usuarioRepository.existsById(idReceita)) {
            List<Receita> todasReceitas = receitaRepository.findAll();
            List<Receita> todasReceitassDeletadas = new ArrayList();
            for (int i = 0; i < todasReceitas.size(); i++) {
                if (todasReceitas.get(i).getFkUsuario() == idReceita) {
                    todasReceitassDeletadas.add(todasReceitas.get(i));
                    receitaRepository.deleteById(todasReceitas.get(i).getCodigo());
                }
            }
            if (todasReceitassDeletadas.size() == 0) {
                return ResponseEntity.status(202).build();
            }
            return ResponseEntity.status(200).body(todasReceitassDeletadas);
        }
        return ResponseEntity.status(404).build();
    }

}

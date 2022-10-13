package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/objetivos")
public class ObjetivoController {
    //get, post,put,delete TEM QUE SER PARA O USUÁRIO QUE ESTÁ RELACIONADO
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ObjetivoRepository objetivoRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Objetivo>> listarTodosObjetivosDoUsuario(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            List<Objetivo> todosObjetivos = objetivoRepository.findAll();
            List<Objetivo> todosObjetivosDoUsuario = new ArrayList<>();
            for (Objetivo objetivoAtual : todosObjetivos) {
                if (objetivoAtual.getFkUsuario() == idUsuario) {
                    todosObjetivosDoUsuario.add(objetivoAtual);
                }
            }
            if (todosObjetivosDoUsuario.isEmpty()) {
                return ResponseEntity.status(204).build();
            }
            return ResponseEntity.status(200).body(todosObjetivosDoUsuario);
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> listarTodosObjetivosDoUsuario(@PathVariable int idUsuario, @PathVariable int idObjetivo) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            List<Objetivo> todosObjetivos = objetivoRepository.findAll();
            for (Objetivo objetivoAtual : todosObjetivos) {
                if (objetivoAtual.getFkUsuario() == idUsuario) {
                    return ResponseEntity.status(200).body(objetivoAtual);
                }
            }
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Objetivo> criarObjetivo(@PathVariable int idUsuario, @RequestBody Objetivo objetivo) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            objetivo.setUsuario(usuario.get());
            return ResponseEntity.status(201).body(objetivoRepository.save(objetivo));
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> atualizarObjetivo(@PathVariable int idUsuario, @PathVariable int idObjetivo,
                                                      @RequestBody Objetivo objetivo) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            objetivo.setUsuario(usuario.get());
            return ResponseEntity.status(200).body(objetivoRepository.save(objetivo));
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> deletarObjetivo(@PathVariable int idUsuario, @PathVariable int idObjetivo) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            Optional<Objetivo> objetivo = objetivoRepository.findById(idObjetivo);
            objetivoRepository.deleteById(idObjetivo);
            return ResponseEntity.status(200).body(objetivo.get());
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/deletarTodos/{idUsuario}")
    public ResponseEntity<List<Objetivo>> deletarTodosObjetivos(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            List<Objetivo> todosObjetivos = objetivoRepository.findAll();
            List<Objetivo> todosObjetivosDeletados = new ArrayList();
            for (int i = 0; i < todosObjetivos.size(); i++) {
                if (todosObjetivos.get(i).getFkUsuario() == idUsuario) {
                    todosObjetivosDeletados.add(todosObjetivos.get(i));
                    objetivoRepository.deleteById(todosObjetivos.get(i).getCodigo());
                }
            }
            if (todosObjetivosDeletados.size() == 0) {
                return ResponseEntity.status(202).build();
            }
            return ResponseEntity.status(200).body(todosObjetivosDeletados);
        }
        return ResponseEntity.status(404).build();
    }
}

package com.projetopi.loginlogoff.financas.despesa;

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
@RequestMapping("/despesas")
public class DespesaController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DespesaRepository despesaRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Despesa>> listarTodasDespesasDoUsuario(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            List<Despesa> todasDespesas = despesaRepository.findAll();
            List<Despesa> todasDespesasDoUsuario = new ArrayList<>();
            for (Despesa despesaAtual : todasDespesas) {
                if (despesaAtual.getFkUsuario() == idUsuario) {
                    todasDespesasDoUsuario.add(despesaAtual);
                }
            }
            if (todasDespesasDoUsuario.isEmpty()) {
                return ResponseEntity.status(204).build();
            }
            return ResponseEntity.status(200).body(todasDespesasDoUsuario);
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/{idUsuario}/{idDespesa}")
    public ResponseEntity<Despesa> listarTodasDespesasDoUsuario(@PathVariable int idUsuario, @PathVariable int idDespesa) {
        if (usuarioRepository.existsById(idUsuario) && despesaRepository.existsById(idDespesa)) {
            List<Despesa> todasDespesas = despesaRepository.findAll();
            for (Despesa despesaAtual : todasDespesas) {
                if (despesaAtual.getFkUsuario() == idUsuario && despesaAtual.getCodigo() == idDespesa) {
                    return ResponseEntity.status(200).body(despesaAtual);
                }
            }
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Despesa> criarDespesa(@PathVariable int idUsuario,@Valid @RequestBody Despesa despesa) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            despesa.setUsuario(usuario.get());
            return ResponseEntity.status(201).body(despesaRepository.save(despesa));
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{idUsuario}/{idDespesa}")
    public ResponseEntity<Despesa> atualizarDespesa(@PathVariable int idUsuario, @PathVariable int idDespesa,
                                                    @Valid @RequestBody Despesa despesa) {
        if (usuarioRepository.existsById(idUsuario) && despesaRepository.existsById(idDespesa)) {
            Despesa despesaAtualizada = new Despesa(
                    idDespesa,
                    despesa.getNome(),
                    despesa.getDescricao(),
                    despesa.getValor(),
                    despesa.getData(),
                    despesa.getCategoria(),
                    despesa.isPago(),
                    despesa.getQtdParcelas()
            );
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            despesaAtualizada.setUsuario(usuario.get());
            return ResponseEntity.status(200).body(despesaRepository.save(despesaAtualizada));
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{idUsuario}/{idDespesa}")
    public ResponseEntity<Despesa> deletarDespesa(@PathVariable int idUsuario, @PathVariable int idDespesa) {
        if (usuarioRepository.existsById(idUsuario) && despesaRepository.existsById(idDespesa)) {
            Optional<Despesa> despesa = despesaRepository.findById(idDespesa);
            despesaRepository.deleteById(idDespesa);
            return ResponseEntity.status(200).body(despesa.get());
        }
        return ResponseEntity.status(404).build();
    }
    @DeleteMapping("/deletarTodas/{idDespesa}")
    public ResponseEntity<List<Despesa>> deletarTodasDespesas(@PathVariable int idDespesa) {
        if (usuarioRepository.existsById(idDespesa)) {
            List<Despesa> todasDespesas = despesaRepository.findAll();
            List<Despesa> todasDespesassDeletadas = new ArrayList();
            for (int i = 0; i < todasDespesas.size(); i++) {
                if (todasDespesas.get(i).getFkUsuario() == idDespesa) {
                    todasDespesassDeletadas.add(todasDespesas.get(i));
                    despesaRepository.deleteById(todasDespesas.get(i).getCodigo());
                }
            }
            if (todasDespesassDeletadas.size() == 0) {
                return ResponseEntity.status(202).build();
            }
            return ResponseEntity.status(200).body(todasDespesassDeletadas);
        }
        return ResponseEntity.status(404).build();
    }

}

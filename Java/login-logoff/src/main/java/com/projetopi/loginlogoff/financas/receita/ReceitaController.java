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
    Log log = new Log();

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
                ResponseEntity respostaVazio = ResponseEntity.status(204).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaVazio;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todasReceitasDoUsuario);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @GetMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity<Receita> listarTodasReceitasDoUsuario(@PathVariable int idUsuario, @PathVariable int idReceita) {
        if (usuarioRepository.existsById(idUsuario) && receitaRepository.existsById(idReceita)) {
            List<Receita> todasReceitas = receitaRepository.findAll();
            for (Receita receitaAtual : todasReceitas) {
                if (receitaAtual.getFkUsuario() == idUsuario && receitaAtual.getCodigo() == idReceita) {
                    ResponseEntity respostaOk = ResponseEntity.status(200).body(receitaAtual);
                    String statusCode = respostaOk.getStatusCode().toString();
                    String logResposta = respostaOk.getStatusCode().series().toString();
                    String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
                    log.gravaLog(textoLog);
                    return respostaOk;
                }
            }
            ResponseEntity respostaVazio = ResponseEntity.status(204).build();
            String statusCode = respostaVazio.getStatusCode().toString();
            String logResposta = respostaVazio.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaVazio;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Receita> criarReceita(@Valid @PathVariable int idUsuario, @RequestBody Receita receita) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            receita.setUsuario(usuario.get());
            ResponseEntity respostaOk = ResponseEntity.status(201).body(receitaRepository.save(receita));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: criarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: criarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
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
            ResponseEntity respostaOk = ResponseEntity.status(200).body(receitaRepository.save(receitaAtualizada));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: atualizarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: atualizarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @DeleteMapping("/{idUsuario}/{idReceita}")
    public ResponseEntity<Receita> deletarReceita(@PathVariable int idUsuario, @PathVariable int idReceita) {
        if (usuarioRepository.existsById(idUsuario) && receitaRepository.existsById(idReceita)) {
            Optional<Receita> receita = receitaRepository.findById(idReceita);
            receitaRepository.deleteById(idReceita);
            ResponseEntity respostaOk = ResponseEntity.status(200).body(receita.get());
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
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
                ResponseEntity respostaVazio = ResponseEntity.status(202).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: deletarTodasReceitas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
                log.gravaLog(textoLog);
                return respostaVazio;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todasReceitassDeletadas);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarTodasReceitas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarTodasReceitas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
        log.gravaLog(textoLog);
        return respostaErro;
    }

}

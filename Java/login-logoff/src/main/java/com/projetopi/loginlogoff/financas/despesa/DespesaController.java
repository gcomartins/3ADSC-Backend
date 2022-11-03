package com.projetopi.loginlogoff.financas.despesa;

import com.projetopi.loginlogoff.Log;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    Log log = new Log();

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
                ResponseEntity respostaVazio = ResponseEntity.status(204).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\nENDPOINT: listarTodasDespesasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaVazio;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todasDespesasDoUsuario);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\nENDPOINT: listarTodasDespesasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\nENDPOINT: listarTodasDespesasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @GetMapping("/{idUsuario}/{idDespesa}")
    public ResponseEntity<Despesa> listarTodasDespesasDoUsuario(@PathVariable int idUsuario, @PathVariable int idDespesa) {
        if (usuarioRepository.existsById(idUsuario) && despesaRepository.existsById(idDespesa)) {
            List<Despesa> todasDespesas = despesaRepository.findAll();
            for (Despesa despesaAtual : todasDespesas) {
                if (despesaAtual.getFkUsuario() == idUsuario && despesaAtual.getCodigo() == idDespesa) {
                    ResponseEntity respostaOk = ResponseEntity.status(200).body(despesaAtual);
                    String statusCode = respostaOk.getStatusCode().toString();
                    String logResposta = respostaOk.getStatusCode().series().toString();
                    String textoLog = "\nENDPOINT: listarTodasDespesasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
                    log.gravaLog(textoLog);
                    return respostaOk;
                }
            }
            ResponseEntity respostaVazio = ResponseEntity.status(204).build();
            String statusCode = respostaVazio.getStatusCode().toString();
            String logResposta = respostaVazio.getStatusCode().series().toString();
            String textoLog = "\nENDPOINT: listarTodasDespesasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "idUsuario: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaVazio;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\nENDPOINT: listarTodasDespesasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Despesa> criarDespesa(@PathVariable int idUsuario, @Valid @RequestBody Despesa despesa) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            despesa.setUsuario(usuario.get());
            ResponseEntity respostaOk = ResponseEntity.status(201).body(despesaRepository.save(despesa));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\nENDPOINT: criarDespesa \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\nENDPOINT: criarDespesa \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
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
            ResponseEntity respostaOk = ResponseEntity.status(200).body(despesaRepository.save(despesaAtualizada));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\nENDPOINT: atualizarDespesa \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\nENDPOINT: atualizarDespesa \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @DeleteMapping("/{idUsuario}/{idDespesa}")
    public ResponseEntity<Despesa> deletarDespesa(@PathVariable int idUsuario, @PathVariable int idDespesa) {
        if (usuarioRepository.existsById(idUsuario) && despesaRepository.existsById(idDespesa)) {
            Optional<Despesa> despesa = despesaRepository.findById(idDespesa);
            despesaRepository.deleteById(idDespesa);
            ResponseEntity respostaOk = ResponseEntity.status(200).body(despesa.get());
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\nENDPOINT: deletarDespesa \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\nENDPOINT: deletarDespesa \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
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
                ResponseEntity respostaVazio = ResponseEntity.status(202).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\nENDPOINT: deletarTodasDespesas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
                log.gravaLog(textoLog);
                return respostaVazio;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todasDespesassDeletadas);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\nENDPOINT: deletarTodasDespesas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\nENDPOINT: deletarTodasDespesas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
        log.gravaLog(textoLog);
        return respostaErro;
    }
}

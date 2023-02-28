package com.projetopi.loginlogoff.financas.receita;

import com.projetopi.loginlogoff.Log;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    public ReceitaRepository receitaRepository;
    @Autowired
    public UsuarioRepository usuarioRepository;
    Log log = new Log();

    public List<Receita> listarTodasReceitasDoUsuario(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            List<Receita> todasReceitasDoUsuario = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
            if (todasReceitasDoUsuario.isEmpty()) {
                ResponseEntity respostaVazio = ResponseEntity.status(204).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario;
                log.gravaLog(textoLog);
                return todasReceitasDoUsuario;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todasReceitasDoUsuario);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return todasReceitasDoUsuario;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return null;
    }

    public Optional<Receita> buscarReceitaDoUsuario(@PathVariable int idUsuario, @PathVariable int idReceita) {
        if (usuarioRepository.existsById(idUsuario) && receitaRepository.existsById(idReceita)) {
            Optional<Receita> receita = receitaRepository.findByUsuarioIdAndCodigo(idUsuario, idReceita);
            if (receita != null) {
                ResponseEntity respostaOk = ResponseEntity.status(200).body(receita);
                String statusCode = respostaOk.getStatusCode().toString();
                String logResposta = respostaOk.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: "
                        + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario + "\nidReceita: " + idReceita;
                log.gravaLog(textoLog);
                return receita;
            }
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: listarTodasReceitasDoUsuario \nStatus Code: " + statusCode + "\nLog: "
                + logResposta + "\nidUsuário: " + idUsuario + "\nidReceita: " + idReceita;
        log.gravaLog(textoLog);
        return null;
    }

    public Receita criarReceita(@Valid @PathVariable int idUsuario, @RequestBody Receita receita) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            receita.setUsuario(usuario.get());
            ResponseEntity respostaOk = ResponseEntity.status(201).body(receitaRepository.save(receita));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: criarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return receita;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: criarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return null;
    }

    public Receita atualizarReceita(@Valid @PathVariable int idUsuario, @PathVariable int idReceita,
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
            return receitaAtualizada;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: atualizarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return null;
    }

    public Optional deletarReceita(@PathVariable int idUsuario, @PathVariable int idReceita) {
        if (usuarioRepository.existsById(idUsuario) && receitaRepository.existsById(idReceita)) {
            Optional<Receita> receita = receitaRepository.findById(idReceita);
            receitaRepository.deleteById(idReceita);
            ResponseEntity respostaOk = ResponseEntity.status(200).body(receita.get());
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return receita;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarReceita \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return null;
    }

    public List<Receita> deletarTodasReceitas(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            List<Receita> todasReceitasDoUsuario = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
            if (todasReceitasDoUsuario.size() == 0) {
                ResponseEntity respostaVazio = ResponseEntity.status(202).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: deletarTodasReceitas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
                log.gravaLog(textoLog);
                return todasReceitasDoUsuario;
            }
            for (int i = 0; i < todasReceitasDoUsuario.size(); i++) {
                receitaRepository.deleteById(todasReceitasDoUsuario.get(i).getCodigo());
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todasReceitasDoUsuario);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarTodasReceitas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
            log.gravaLog(textoLog);
            return todasReceitasDoUsuario;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarTodasReceitas \nStatus Code: " + statusCode + "\nLog: " + logResposta;
        log.gravaLog(textoLog);
        return null;
    }


}

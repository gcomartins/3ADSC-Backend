package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.Log;
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
@RequestMapping("/objetivos")
public class ObjetivoController {
    //get, post,put,delete TEM QUE SER PARA O USUÁRIO QUE ESTÁ RELACIONADO
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ObjetivoRepository objetivoRepository;

    Log log = new Log();
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Objetivo>> listarTodosObjetivosDoUsuario(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            List<Objetivo> todosObjetivosDoUsuario = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
            if (todosObjetivosDoUsuario.isEmpty()) {
                ResponseEntity respostaVazio = ResponseEntity.status(204).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: listarTodosObjetivosDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaVazio;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todosObjetivosDoUsuario);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: listarTodosObjetivosDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: listarTodosObjetivosDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @GetMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> exibirObjetivoDoUsuario(@PathVariable int idUsuario, @PathVariable int idObjetivo) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            List<Objetivo> objetivoUsuario = objetivoRepository.findByUsuarioIdAndCodigoOrderByData(idUsuario,idObjetivo);
            if (objetivoUsuario.isEmpty()){
                ResponseEntity respostaVazio = ResponseEntity.status(204).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: exibirObjetivoDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaVazio;
            }else {
                ResponseEntity respostaOk = ResponseEntity.status(200).body(objetivoUsuario.get(0));
                String statusCode = respostaOk.getStatusCode().toString();
                String logResposta = respostaOk.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: exibirObjetivoDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaOk;
            }
        }
//        if (!usuarioRepository.existsById(idUsuario) && !objetivoRepository.existsById(idObjetivo)){
//            log.gravaLog("Exibir objetivo do usuário: Usuário e Objetivo não identificados");
//        }else if (!usuarioRepository.existsById(idUsuario)){
//            log.gravaLog("Exibir objetivo do usuário: Usuário não identificado");
//        }else if (!objetivoRepository.existsById(idObjetivo)){
//            log.gravaLog("Exibir objetivo do usuário: Objetivo não identificado");
//        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: exibirObjetivoDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario + "\nidObjetivo: " + idObjetivo;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Objetivo> criarObjetivo(@PathVariable int idUsuario,@Valid @RequestBody Objetivo objetivo) {
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            objetivo.setUsuario(usuario.get());
            ResponseEntity respostaOk = ResponseEntity.status(201).body(objetivoRepository.save(objetivo));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: criarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: criarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @PutMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> atualizarObjetivo(@PathVariable int idUsuario, @PathVariable int idObjetivo,
                                                      @Valid @RequestBody Objetivo objetivo) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            Objetivo objetivoAtualizado = new Objetivo(
                    idObjetivo,
                    objetivo.getNome(),
                    objetivo.getDescricao(),
                    objetivo.getValor(),
                    objetivo.getData(),
                    objetivo.getCategoria(),
                    objetivo.getValorAtual(),
                    objetivo.getDataFinal()
            );
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            objetivoAtualizado.setUsuario(usuario.get());
            ResponseEntity respostaOk = ResponseEntity.status(200).body(objetivoRepository.save(objetivoAtualizado));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: atualizarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario + "\nidUObjetivo: " + idObjetivo;
            log.gravaLog(textoLog);
            return respostaOk;
        }
//        if (!usuarioRepository.existsById(idUsuario) && !objetivoRepository.existsById(idObjetivo)){
//            log.gravaLog("Atualizar objetivo para o usuário: Usuário e Objetivo não identificados");
//        }else if (!usuarioRepository.existsById(idUsuario)){
//            log.gravaLog("Atualizar objetivo para o usuário: Usuário não identificado");
//        }else if (!objetivoRepository.existsById(idObjetivo)){
//            log.gravaLog("Atualizar objetivo para o usuário: Objetivo não identificado");
//        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: atualizarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario + "\nidUObjetivo: " + idObjetivo;
        log.gravaLog(textoLog);
        return respostaErro;
    }

    @DeleteMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> deletarObjetivo(@PathVariable int idUsuario, @PathVariable int idObjetivo) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            Optional<Objetivo> objetivo = objetivoRepository.findById(idObjetivo);
            objetivoRepository.deleteById(idObjetivo);
            ResponseEntity respostaOk = ResponseEntity.status(200).body(objetivo.get());
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario + "\nidObjetivo: " + idObjetivo;
            log.gravaLog(textoLog);
            return respostaOk;
        }
//        if (!usuarioRepository.existsById(idUsuario) && !objetivoRepository.existsById(idObjetivo)){
//            log.gravaLog("Deletar objetivo para o usuário: Usuário e Objetivo não identificados");
//        }else if (!usuarioRepository.existsById(idUsuario)){
//            log.gravaLog("Deletar objetivo para o usuário: Usuário não identificado");
//        }else if (!objetivoRepository.existsById(idObjetivo)){
//            log.gravaLog("Deletar objetivo para o usuário: Objetivo não identificado");
//        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario + "\nidObjetivo: " + idObjetivo;
        log.gravaLog(textoLog);
        return respostaErro;
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
                ResponseEntity respostaVazio = ResponseEntity.status(202).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: deletarTodosObjetivos \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaVazio;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todosObjetivosDeletados);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarTodosObjetivos \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarTodosObjetivos \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }
}

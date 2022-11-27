package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.Log;
import com.projetopi.loginlogoff.PilhaObj;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
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
                ResponseEntity respostaOk = ResponseEntity.status(200).body(objetivoUsuario.get(0));
                String statusCode = respostaOk.getStatusCode().toString();
                String logResposta = respostaOk.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: exibirObjetivoDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaOk;
        }
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
            List<Objetivo> todosObjetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
            if (todosObjetivos.size() == 0) {
                ResponseEntity respostaVazio = ResponseEntity.status(202).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: deletarTodosObjetivos \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
                log.gravaLog(textoLog);
                return respostaVazio;
            }
            for (int i = 0; i < todosObjetivos.size(); i++) {
                objetivoRepository.deleteById(todosObjetivos.get(i).getCodigo());
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todosObjetivos);
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
    @DeleteMapping("/pilha/{idUsuario}")
    public ResponseEntity<Objetivo> deletarObjetivoPilha(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.countByUsuarioId(idUsuario) > 0){
            List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByCodigo(idUsuario);
            PilhaObj<Objetivo> pilhaDeObjetivo= new PilhaObj<Objetivo>(objetivos.size());
            for (int i = 0; i<objetivos.size();i++){
                pilhaDeObjetivo.push(objetivos.get(i));
            }
            Objetivo objetivoDeletado = pilhaDeObjetivo.peek();
            objetivoRepository.deleteById(objetivoDeletado.getCodigo());
            ResponseEntity respostaOk = ResponseEntity.status(200).body(pilhaDeObjetivo.pop());
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return respostaOk;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return respostaErro;
    }
}
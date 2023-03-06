package com.projetopi.loginlogoff.financas.objetivo;
import com.projetopi.loginlogoff.Log;
import com.projetopi.loginlogoff.mensageria.MensageriaObserver;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObjetivoService {
    @Autowired
    private ObjetivoRepository objetivoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    ObjetivoSubject objetivoSubject = new ObjetivoSubject();
    Log log = new Log();

    public Objetivo criarObjetivo(int idUsuario, Objetivo objetivo){
        if (usuarioRepository.existsById(idUsuario)) {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            objetivo.setUsuario(usuario.get());
            ResponseEntity respostaOk = ResponseEntity.status(201).body(objetivoRepository.save(objetivo));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: criarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            objetivoSubject.setValorInicialObjetivo(objetivo.getValorAtual());
            objetivoSubject.setValorFinalObjetivo(objetivo.getValor());
            return objetivo;
        }else {
            ResponseEntity respostaErro = ResponseEntity.status(404).build();
            String statusCode = respostaErro.getStatusCode().toString();
            String logResposta = respostaErro.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: criarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return null;
        }

    }

    public Objetivo atualizarObjetivo(int idUsuario, int idObjetivo, Objetivo objetivo){
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
            objetivoSubject.setValorInicialObjetivo(objetivoAtualizado.getValorAtual());
            objetivoSubject.setValorFinalObjetivo(objetivoAtualizado.getValor());
            objetivoSubject.notificaObservadores();
            return objetivoAtualizado;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: atualizarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario + "\nidObjetivo: " + idObjetivo;
        log.gravaLog(textoLog);
        return null;
    };

    public  List<Objetivo> listarObjsUsuario( int idUsuario){
        if (usuarioRepository.existsById(idUsuario)) {
            List<Objetivo> todosObjetivosDoUsuario = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
            if (todosObjetivosDoUsuario.isEmpty()) {
                ResponseEntity respostaVazio = ResponseEntity.status(204).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: listarTodosObjetivosDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario;
                log.gravaLog(textoLog);
                return todosObjetivosDoUsuario;
            }
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todosObjetivosDoUsuario);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: listarTodosObjetivosDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return todosObjetivosDoUsuario;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: listarTodosObjetivosDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return null;
    }

    public Objetivo exibirObjetivoUnico(int idUsuario, int idObjetivo){
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            List<Objetivo> objetivoUsuario = objetivoRepository.findByUsuarioIdAndCodigoOrderByData(idUsuario, idObjetivo);
            ResponseEntity respostaOk = ResponseEntity.status(200).body(objetivoUsuario.get(0));
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: exibirObjetivoDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);

            MensageriaObserver observer = new MensageriaObserver();
            objetivoSubject.adicionaObservador(observer);
            return objetivoUsuario.get(0);
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: exibirObjetivoDoUsuario \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuario: " + idUsuario + "\nidObjetivo: " + idObjetivo;
        log.gravaLog(textoLog);
        return null;

    }

    public Objetivo deletarObjetivo(int idUsuario, int idObjetivo){
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.existsById(idObjetivo)) {
            Optional<Objetivo> objetivo = objetivoRepository.findById(idObjetivo);
            objetivoRepository.deleteById(idObjetivo);
            ResponseEntity respostaOk = ResponseEntity.status(200).body(objetivo.get());
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario + "\nidObjetivo: " + idObjetivo;
            log.gravaLog(textoLog);
            return objetivo.get();
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarObjetivo \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario + "\nidObjetivo: " + idObjetivo;
        log.gravaLog(textoLog);
        return null;

    }

    public List<Objetivo> deletarTodosObjetivos(int idUsuario){
        if (usuarioRepository.existsById(idUsuario)) {
            List<Objetivo> todosObjetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
            if (todosObjetivos.size() == 0) {
                ResponseEntity respostaVazio = ResponseEntity.status(202).build();
                String statusCode = respostaVazio.getStatusCode().toString();
                String logResposta = respostaVazio.getStatusCode().series().toString();
                String textoLog = "\n-------------------- \nENDPOINT: deletarTodosObjetivos \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
                log.gravaLog(textoLog);
                return todosObjetivos;
            }
            objetivoRepository.deleteAll();
            ResponseEntity respostaOk = ResponseEntity.status(200).body(todosObjetivos);
            String statusCode = respostaOk.getStatusCode().toString();
            String logResposta = respostaOk.getStatusCode().series().toString();
            String textoLog = "\n-------------------- \nENDPOINT: deletarTodosObjetivos \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
            log.gravaLog(textoLog);
            return todosObjetivos;
        }
        ResponseEntity respostaErro = ResponseEntity.status(404).build();
        String statusCode = respostaErro.getStatusCode().toString();
        String logResposta = respostaErro.getStatusCode().series().toString();
        String textoLog = "\n-------------------- \nENDPOINT: deletarTodosObjetivos \nStatus Code: " + statusCode + "\nLog: " + logResposta + "\nidUsuário: " + idUsuario;
        log.gravaLog(textoLog);
        return null;
    }

}

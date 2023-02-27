package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.Log;
import com.projetopi.loginlogoff.PilhaObj;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.mensageria.MensageriaObserver;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import net.bytebuddy.asm.Advice;
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
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ObjetivoRepository objetivoRepository;
    private ObjetivoSubject objetivoSubject = new ObjetivoSubject();
    @Autowired
    private ObjetivoService objetivoService;
    Log log = new Log();

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Objetivo>> listarTodosObjetivosDoUsuario(@PathVariable int idUsuario) {
        List<Objetivo> resposta = objetivoService.listarObjsUsuario(idUsuario);
        if (resposta != null){
            return resposta.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.status(200).body(resposta);

        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> exibirObjetivoDoUsuario(@PathVariable int idUsuario, @PathVariable int idObjetivo) {
        Objetivo resposta = objetivoService.exibirObjetivoUnico(idUsuario,idObjetivo);
        return resposta != null ? ResponseEntity.status(200).body(resposta) : ResponseEntity.status(404).build();
        }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Objetivo> criarObjetivo(@PathVariable int idUsuario, @Valid @RequestBody Objetivo objetivo) {
        Objetivo resposta = objetivoService.criarObjetivo(idUsuario,objetivo);
        return resposta != null ? ResponseEntity.status(201).body(objetivo) : ResponseEntity.status(400).build();
    }

    @PutMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> atualizarObjetivo(@PathVariable int idUsuario, @PathVariable int idObjetivo,
                                                      @Valid @RequestBody Objetivo objetivo) {
        Objetivo resposta = objetivoService.atualizarObjetivo(idUsuario,idObjetivo,objetivo);
        if (resposta != null) return ResponseEntity.status(200).body(resposta);
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{idUsuario}/{idObjetivo}")
    public ResponseEntity<Objetivo> deletarObjetivo(@PathVariable int idUsuario, @PathVariable int idObjetivo) {
        Objetivo resposta = objetivoService.deletarObjetivo(idObjetivo, idObjetivo);
      return resposta  != null ? ResponseEntity.status(200).body(resposta) : ResponseEntity.status(404).build();

    }

    @DeleteMapping("/deletarTodos/{idUsuario}")
    public ResponseEntity<List<Objetivo>> deletarTodosObjetivos(@PathVariable int idUsuario) {
        List<Objetivo> resposta = objetivoService.deletarTodosObjetivos(idUsuario);
        if (resposta != null) return resposta.isEmpty() ? ResponseEntity.status(202).build() :
                ResponseEntity.status(200).body(resposta);
        else return ResponseEntity.status(404).build();

    }

    @DeleteMapping("/pilha/{idUsuario}")
    public ResponseEntity<Objetivo> deletarObjetivoPilha(@PathVariable int idUsuario) {
        if (usuarioRepository.existsById(idUsuario) && objetivoRepository.countByUsuarioId(idUsuario) > 0) {
            List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByCodigo(idUsuario);
            PilhaObj<Objetivo> pilhaDeObjetivo = new PilhaObj<Objetivo>(objetivos.size());
            for (int i = 0; i < objetivos.size(); i++) {
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
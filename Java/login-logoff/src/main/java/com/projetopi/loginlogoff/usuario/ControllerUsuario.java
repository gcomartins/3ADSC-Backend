package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.despesa.DespesaRepository;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.objetivo.ObjetivoRepository;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.receita.ReceitaRepository;
import com.projetopi.loginlogoff.usuario.ServiceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ObjetivoRepository objetivoRepository;
    @Autowired
    private  ReceitaRepository receitaRepository;
    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private ServiceUsuario serviceUsuario;


    @PostMapping
    public ResponseEntity<Usuario> cadastrar( @Valid  @RequestBody Usuario usuarioNovo) {
        Usuario usuario = usuarioRepository.findByCpf(usuarioNovo.getCpf());
        Usuario usuario1 = usuarioRepository.findByEmail(usuarioNovo.getEmail());
        if (usuario != null || usuario1 != null){
            System.out.println("Cpf ou Email ja utilizados");
            return ResponseEntity.status(400).build();
        }
        usuarioNovo.setAutenticado(false);
        Usuario usuarioCadastrado = this.usuarioRepository.save(usuarioNovo);
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.status(200).body(usuarios);
    }


    @PutMapping("login/{email}/{senha}")
    public ResponseEntity<Usuario> login(@PathVariable String email, @PathVariable String senha) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(email,senha);

        if(usuario != null) {
            usuario.setAutenticado(true);
            usuarioRepository.save(usuario);
            return ResponseEntity.status(200).body(usuario);
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("logoff/{email}/{senha}")
    public ResponseEntity<Usuario> logoff(@PathVariable String email, @PathVariable String senha) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(email,senha);
        if (usuario == null) return ResponseEntity.status(404).build();
        if(usuario.getAutenticado()) {
            usuario.setAutenticado(false);
            usuarioRepository.save(usuario);
            return ResponseEntity.status(200).body(usuario);
        } else if (!usuario.getAutenticado()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{senhaAntiga}/{emailAntigo}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String emailAntigo, @PathVariable String senhaAntiga,
                                                   @Valid @RequestBody Usuario usuarioAtualizado ) {
           Boolean isAtualizado = serviceUsuario.atualizarUsuario(emailAntigo,senhaAntiga,usuarioAtualizado);
           if(isAtualizado) return ResponseEntity.status(202).body(usuarioAtualizado);
            return ResponseEntity.status(400).build();
        }
    @PostMapping("/gerarCsv/{idUsuario}/{nomeArquivo}")
    public ResponseEntity<String> gerarCsv(@PathVariable Integer idUsuario,@PathVariable  String nomeArquivo){
        String retorno = serviceUsuario.gerarCsv(idUsuario,nomeArquivo);
        if (retorno.contains("arquivo gerado com sucesso")) return ResponseEntity.status(200).body(retorno);
           return ResponseEntity.status(400).body(retorno);
    }

    @PostMapping("/gerarTxt/{idUsuario}/{nomeArquivo}")
    public ResponseEntity<String> gerarTxt(@PathVariable Integer idUsuario,@PathVariable  String nomeArquivo){
        String retorno = serviceUsuario.gerarTxt(idUsuario,nomeArquivo);
        if (retorno.contains("arquivo gravado com sucesso"))
            return ResponseEntity.status(201).body(retorno);
        return ResponseEntity.status(400).body(retorno);
    }
    @PostMapping("/lerTxt/{idUsuario}/{nomeArquivo}")
    public ResponseEntity<String> lerTxt(@PathVariable Integer idUsuario, @PathVariable String nomeArquivo){
        String retorno = serviceUsuario.leArquivoTxt(nomeArquivo,idUsuario);

      return   ResponseEntity.status(200).body(retorno);

    }

}



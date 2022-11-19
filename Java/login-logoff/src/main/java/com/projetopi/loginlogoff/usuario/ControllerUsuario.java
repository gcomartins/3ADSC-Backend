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
    public ResponseEntity<Usuario> put(@PathVariable String emailAntigo, @PathVariable String senhaAntiga,
                                                   @Valid @RequestBody Usuario usuarioAtualizado ) {
           Boolean isAtualizado = serviceUsuario.atualizarUsuario(emailAntigo,senhaAntiga,usuarioAtualizado);
           if(isAtualizado) return ResponseEntity.status(202).body(usuarioAtualizado);
            return ResponseEntity.status(400).build();
        }
    @PostMapping("/gerarCsv/{idUsuario}/{nomeArquivo}")
    public ResponseEntity<String> gerarCsv(@PathVariable Integer idUsuario,@PathVariable  String nomeArquivo){
        // pegando tudo o que precisa do banco
        List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Receita> receitas = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Despesa> despesas = despesaRepository.findByUsuarioIdOrderByData(idUsuario);
        // aqui pegando o tamanho dos itens para passar como parametro quando transforma-los em vetor
        // criando objetos do tipo ListObj
        ListaObj listaReceitas = new ListaObj<>(receitaRepository.countByUsuarioId(idUsuario));
        ListaObj listaObjetos = new ListaObj<>(objetivoRepository.countByUsuarioId(idUsuario));
        ListaObj listaDespesa = new ListaObj<>(despesaRepository.countByUsuarioId(idUsuario));

        // adicionando valores a eles
        for (Receita receitaAtual: receitas){
            listaReceitas.adiciona(receitaAtual);
        }
        for (Objetivo objetivoAtual : objetivos){
            listaObjetos.adiciona(objetivoAtual);
        }

        for (Despesa despesaAtual: despesas ){
            listaDespesa.adiciona(despesaAtual);
        }
        // gravando as informações nos arquivos
        listaObjetos.gravaArquivoCsvObjetivo(listaObjetos,listaReceitas,listaDespesa, nomeArquivo);
        return listaObjetos.gravaArquivoCsvObjetivo(listaObjetos,listaReceitas,listaDespesa, nomeArquivo);
    }

    @PostMapping("/gerarTxt/{idUsuario}/{nomeArquivo}")
    public ResponseEntity<String> gerarTxt(@PathVariable Integer idUsuario,@PathVariable  String nomeArquivo){
        List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
        ListaObj listaObjetivos = new ListaObj<>(objetivoRepository.countByUsuarioId(idUsuario));
        for (Objetivo objetivoAtual: objetivos){
            listaObjetivos.adiciona(objetivoAtual);
        }
        List<Receita> receitas = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
        ListaObj listaReceitas = new ListaObj<>(receitaRepository.countByUsuarioId(idUsuario));
        for (Receita receitaAtual: receitas){
            listaReceitas.adiciona(receitaAtual);
        }
        List<Despesa> despesas = despesaRepository.findByUsuarioIdOrderByData(idUsuario);
        ListaObj listaDespesas = new ListaObj<>(despesaRepository.countByUsuarioId(idUsuario));
        for (Despesa despesaAtual: despesas ){
            listaDespesas.adiciona(despesaAtual);
        }
        return ResponseEntity.status(201).body(listaObjetivos.gravaArquivoTxt(listaObjetivos,listaReceitas,listaDespesas, nomeArquivo));


    }
}



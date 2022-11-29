package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.despesa.DespesaRepository;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.objetivo.ObjetivoRepository;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.receita.ReceitaRepository;
import com.projetopi.loginlogoff.financas.RelatorioMensal;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
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
        Usuario usuario = usuarioRepository.findByEmail(usuarioNovo.getEmail());
        if (usuario != null){
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
    @GetMapping("/historicoReceitasMensal/{idUsuario}")
    public ResponseEntity<List<Receita>> historicoReceita(@PathVariable Integer idUsuario,
                                                          @RequestParam(required = false) Integer mes,
                                                          @RequestParam(required = false) Integer ano){
        List<Receita> receitas = serviceUsuario.getHistoricoReceita(idUsuario,mes,ano);
        if (!receitas.isEmpty())
            return ResponseEntity.status(200).body(receitas);
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/historicoDespesasMensal/{idUsuario}")
    public ResponseEntity<List<Despesa>> historicoDespesa(@PathVariable Integer idUsuario,
                                                          @RequestParam(required = false) Integer mes,
                                                          @RequestParam(required = false) Integer ano){
        List<Despesa> despesas = serviceUsuario.getHistoricoDespesa(idUsuario,mes,ano);
            return ResponseEntity.status(200).body(despesas);
    }



    @GetMapping("/valoresGraficoReceita/{idUsuario}")
    public List<RelatorioMensal> datasReceitas(@PathVariable Integer idUsuario){
            List<RelatorioMensal> relatorio = serviceUsuario.getRelatorioGeralByData(idUsuario);
       return relatorio ;
    }

    @GetMapping("/valorSaldoMensal/{idUsuario}")
    public ResponseEntity<Double> historicoReceitaUnica(@PathVariable Integer idUsuario,
                                                          @RequestParam(required = false) Integer mes,
                                                          @RequestParam(required = false) Integer ano){

            return ResponseEntity.status(200).body(serviceUsuario.getSaldoMensal(idUsuario));
    }
}



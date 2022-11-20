package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.ListaObj;
import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.despesa.DespesaRepository;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.objetivo.ObjetivoRepository;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.receita.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.projetopi.loginlogoff.usuario.ControllerUsuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;

import java.util.List;

@Service
public class ServiceUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ObjetivoRepository objetivoRepository;
    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private DespesaRepository despesaRepository;


    public  Boolean atualizarUsuario(String emailAntigo, String senhaAntiga,
                                                     Usuario usuarioAtualizado ) {
        Boolean isAtualizado = false;
        Usuario usuario = usuarioRepository.findByEmailAndSenha(emailAntigo, senhaAntiga);
        if (usuario == null) return isAtualizado;
        if (usuarioAtualizado.getSenha().equals(senhaAntiga) &&
                usuarioAtualizado.getEmail().equals(emailAntigo) &&
                usuarioAtualizado.getNome().equals(usuario.getNome()) ) {
            return isAtualizado;
        }
        System.out.println(usuario);
        usuario.setSenha(usuarioAtualizado.getSenha());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setNome(usuarioAtualizado.getNome());
        usuarioRepository.save(usuario);
        isAtualizado = true;
        return isAtualizado;
    }

    public String gerarCsv(int idUsuario, String nomeArquivo){
        List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Receita> receitas = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Despesa> despesas = despesaRepository.findByUsuarioIdOrderByData(idUsuario);
        // aqui pegando o tamanho dos itens para passar como parametro quando transforma-los em vetor
        // criando objetos do tipo ListObj
        ListaObj listaReceitas = new ListaObj<>(receitaRepository.countByUsuarioId(idUsuario));
        ListaObj listaObjetos = new ListaObj<>(objetivoRepository.countByUsuarioId(idUsuario));
        ListaObj listaDespesa = new ListaObj<>(despesaRepository.countByUsuarioId(idUsuario));
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

    public String gerarTxt(int idUsuario, String nomeArquivo){
        List<Objetivo> objetivos = objetivoRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Receita> receitas = receitaRepository.findByUsuarioIdOrderByData(idUsuario);
        List<Despesa> despesas = despesaRepository.findByUsuarioIdOrderByData(idUsuario);

        ListaObj listaObjetivos = new ListaObj<>(objetivoRepository.countByUsuarioId(idUsuario));
        for (Objetivo objetivoAtual: objetivos){
            listaObjetivos.adiciona(objetivoAtual);
        }
        ListaObj listaReceitas = new ListaObj<>(receitaRepository.countByUsuarioId(idUsuario));
        for (Receita receitaAtual: receitas){
            listaReceitas.adiciona(receitaAtual);
        }
        ListaObj listaDespesas = new ListaObj<>(despesaRepository.countByUsuarioId(idUsuario));
        for (Despesa despesaAtual: despesas ){
            listaDespesas.adiciona(despesaAtual);
        }
        return listaObjetivos.gravaArquivoTxt(listaObjetivos,listaReceitas,listaDespesas, nomeArquivo);


    }

}

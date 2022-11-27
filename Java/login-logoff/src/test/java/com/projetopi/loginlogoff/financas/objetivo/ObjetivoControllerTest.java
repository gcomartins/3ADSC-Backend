package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.financas.receita.ReceitaController;
import com.projetopi.loginlogoff.financas.receita.ReceitaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.projetopi.loginlogoff.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
@SpringBootTest
class ObjetivoControllerTest {

    @Autowired
    private ObjetivoController objetivoController;

    @MockBean
    private ObjetivoRepository objetivoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Nao listar objetivos quando usuario não encontrado")
    void retornar404QuandoUsuarioNaoForEncontrado() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        ResponseEntity response = objetivoController.listarTodosObjetivosDoUsuario(1);
        List<Objetivo> listaObjetivos = objetivoController.listarTodosObjetivosDoUsuario(1).getBody();

        assertEquals(404,response.getStatusCodeValue());
        assertNull(listaObjetivos);
    }

    @Test
    @DisplayName("Retornar lista vazia quando usuario nao possui objetivo")
    void retornarListaVaziaQuandoUsuarioNaoTemObjetivos() {
        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        List<Objetivo> objetivo = new ArrayList<>();
        when(objetivoRepository.findByUsuarioIdOrderByData(anyInt())).thenReturn(objetivo);
        ResponseEntity response = objetivoController.listarTodosObjetivosDoUsuario(1);
        List<Objetivo> listaObjetivos = objetivoController.listarTodosObjetivosDoUsuario(1).getBody();

        assertEquals(204,response.getStatusCodeValue());
        assertNull(listaObjetivos);
    }

    @Test
    @DisplayName("Listar todos objetivoss do usuario quando usuario existe e tem objetivos")
    void listarTodasReceitasDoUsuarioQuandoUsuarioExistsByEtrueEPossuiReceitas() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        List<Objetivo> objetivos = new ArrayList<>();
        objetivos.add(new Objetivo(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",1900,LocalDate.of(2022,11,22)));
        objetivos.add(new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01)));
        objetivos.add(new Objetivo(3,"Play 5","Comprar play 5 pra jogar bom de guerra",
                5200, LocalDate.of(2022,11,30),"Entreterimento",2,LocalDate.of(2023,02,27)));
        when(objetivoRepository.findByUsuarioIdOrderByData(1)).thenReturn(objetivos);

        ResponseEntity response = objetivoController.listarTodosObjetivosDoUsuario(1);
        List<Objetivo> listaObjetivos = objetivoController.listarTodosObjetivosDoUsuario(1).getBody();

        assertNotNull(response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(objetivos,listaObjetivos);
        assertEquals(3,listaObjetivos.size());
    }

    @Test
    @DisplayName("Buscar objetivo do usuario quando usuario existe e há objetivo")
    void exibirObjetivoDoUsuarioQuandoUsuarioExisteEObjetivoExiste() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(objetivoRepository.existsById(2)).thenReturn(true);
        List objetivoList = new ArrayList<>();
        objetivoList.add(new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01)));

        when(objetivoRepository.findByUsuarioIdAndCodigoOrderByData(1,2)).thenReturn(objetivoList);

        ResponseEntity response = objetivoController.exibirObjetivoDoUsuario(1,2);
        Objetivo objetivo = objetivoController.exibirObjetivoDoUsuario(1,2).getBody();

        assertNotNull(response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(2,objetivo.getCodigo());
        assertEquals("Copa do mundo dia 2 só estádio",objetivo.getNome());
        assertEquals("Ver o menino Ney",objetivo.getDescricao());
    }

    @Test
    @DisplayName("Exibir objetivo do usuario quando usuario não existe e há receita")
    void exibirObjetivoDoUsuarioRetorna404QuandoUsuarioNaoExiste() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        when(objetivoRepository.existsById(2)).thenReturn(true);

        ResponseEntity response = objetivoController.exibirObjetivoDoUsuario(1,2);
        Objetivo objetivo = objetivoController.exibirObjetivoDoUsuario(1,2).getBody();

        assertNotNull(response.hasBody());
        assertNull(objetivo);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    @DisplayName("Buscar receita do usuario quando usuario existe e não há a receita")
    void buscarReceitaDoUsuarioRetorna404QuandoReceitaNaoExiste() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(objetivoRepository.existsById(2)).thenReturn(false);

        ResponseEntity response = objetivoController.exibirObjetivoDoUsuario(1,2);
        Objetivo objetivo = objetivoController.exibirObjetivoDoUsuario(1,2).getBody();


        assertNotNull(response.hasBody());
        assertNull(objetivo);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    @DisplayName("Criar objetivo do usuario quando usuario existir")
    void criarObjetivoQuandoUsuarioExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        Objetivo objetivo = new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01));
        when(objetivoRepository.save(objetivo)).thenReturn(objetivo);

        ResponseEntity response = objetivoController.criarObjetivo(1,objetivo);
        Objetivo objetivoCriado = objetivoController.criarObjetivo(1,objetivo).getBody();

        assertNotNull(response.hasBody());
        assertEquals(2,objetivoCriado.getCodigo());
        assertEquals(201,response.getStatusCodeValue());
        assertEquals("Copa do mundo dia 2 só estádio",objetivoCriado.getNome());
        assertEquals("Ver o menino Ney",objetivoCriado.getDescricao());
    }

    @Test
    @DisplayName("Não criar objetivo quando usuario não existir")
    void naoCriarObjetivoQuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        Objetivo objetivo = new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01));
        when(objetivoRepository.save(objetivo)).thenReturn(objetivo);

        ResponseEntity response = objetivoController.criarObjetivo(1,objetivo);


        assertNotNull(response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Atualizar objetivo quando usuario e objetivo existir")
    void atualizaObjetivoQuandoUsuarioEObjetivoExistem() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(objetivoRepository.existsById(2)).thenReturn(true);
        List<Objetivo> objetivos = new ArrayList<>();
        objetivos.add(new Objetivo(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",1900,LocalDate.of(2022,11,22)));
        objetivos.add(new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01)));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        objetivos.get(0).setUsuario(usuario);
        objetivos.get(1).setUsuario(usuario);
      Objetivo objetivoAtualizado = new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3600,LocalDate.of(2022,12,01));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(objetivoRepository.save(any())).thenReturn(objetivoAtualizado);

        ResponseEntity response = objetivoController.atualizarObjetivo(1,2,objetivoAtualizado);
        Objetivo novoObjetivo = objetivoController.atualizarObjetivo(1,2,objetivoAtualizado).getBody();

        assertNotNull(response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(2,novoObjetivo.getCodigo());
        assertEquals("Copa do mundo dia 2 só estádio",novoObjetivo.getNome());
        assertEquals(3600,novoObjetivo.getValor());
        assertEquals(false,novoObjetivo.equals(objetivos.get(1)));
    }


    @Test
    @DisplayName("Não atualiza quando usuario não existir")
    void naoAtualizaQuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        Objetivo objetivo = new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01));

        ResponseEntity response = objetivoController.atualizarObjetivo(1,2,objetivo);
        assertNotNull(response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }
    @Test
    @DisplayName("Não atualiza quando receita não existir")
    void naoAtualizaQuandoReceitaNaoExistir() {
        when(objetivoRepository.existsById(anyInt())).thenReturn(false);
        Objetivo objetivo = new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01));
        ResponseEntity response = objetivoController.atualizarObjetivo(1,2,objetivo);
        assertNotNull(response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deleta objetivo quando usuario e objetivo existem")
    void deletaObjetivoQuandoUsuarioEObjetivoExistem() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(objetivoRepository.existsById(2)).thenReturn(true);

        Objetivo objetivo = new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01));

        when(objetivoRepository.findById(2)).thenReturn(Optional.of(objetivo));

        ResponseEntity response = objetivoController.deletarObjetivo(1,2);
        Objetivo objetivoDeletado = objetivoController.deletarObjetivo(1,2).getBody();

        verify(objetivoRepository, times(2)).deleteById(2);
        assertEquals(true,response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(2,objetivoDeletado.getCodigo());
        assertEquals("Copa do mundo dia 2 só estádio",objetivoDeletado.getNome());
        assertEquals("Ver o menino Ney",objetivoDeletado.getDescricao());
        assertEquals(true,objetivo.equals(objetivoDeletado));
    }

    @Test
    @DisplayName("Não deleta objetivo quando usuario nao existe")
    void naoDeletaObjetivoQuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);

        ResponseEntity response = objetivoController.deletarObjetivo(1,2);

        verify(objetivoRepository, times(0)).deleteById(anyInt());
        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Não deleta receita quando receita nao existir")
    void naoDeletaReceitaQuandoReceitaNaoExistir() {
        when(objetivoRepository.existsById(2)).thenReturn(false);

        ResponseEntity response = objetivoController.deletarObjetivo(1,2);

        verify(objetivoRepository, times(0)).deleteById(anyInt());
        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deletar todos  receitas do usuario")
    void deletarTodosObjetivos() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        List<Objetivo> objetivos = new ArrayList<>();
        objetivos.add(new Objetivo(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",1900,LocalDate.of(2022,11,22)));
        objetivos.add(new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01)));
        objetivos.add(new Objetivo(3,"Play 5","Comprar play 5 pra jogar bom de guerra",
                5200, LocalDate.of(2022,11,30),"Entreterimento",2,LocalDate.of(2023,02,27)));

        when(objetivoRepository.findByUsuarioIdOrderByData(1)).thenReturn(objetivos);

        ResponseEntity response = objetivoController.deletarTodosObjetivos(1);
        List<Objetivo> objetivosDeletados = objetivoController.deletarTodosObjetivos(1).getBody();

        assertEquals(true,response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(3,objetivosDeletados.size());
        verify(objetivoRepository, times(6)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Deletar todos objetivos do usuario retorna vazio caso usuario nao tem objetivo")
    void deletarTodasDespesasRetonaVazioQuandoUsuarioNaoTemDespesa() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        List<Objetivo> objetivos = new ArrayList<>();
        when(objetivoRepository.findByUsuarioIdOrderByData(1)).thenReturn(objetivos);

        ResponseEntity response = objetivoController.deletarTodosObjetivos(1);

        assertEquals(false,response.hasBody());
        assertEquals(202,response.getStatusCodeValue());
        verify(objetivoRepository, times(0)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Deletar todas receitas do usuario retorna 404 quando usuario nao existir")
    void deletarTodasReceitasRetona404QuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);

        ResponseEntity response = objetivoController.deletarTodosObjetivos(1);

        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
        verify(objetivoRepository, times(0)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Deletar objetivo do topo da pilha")
    void deletarObjetivoPilha() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(objetivoRepository.countByUsuarioId(1)).thenReturn(3);
        List<Objetivo> objetivos = new ArrayList<>();
        objetivos.add(new Objetivo(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",1900,LocalDate.of(2022,11,22)));
        objetivos.add(new Objetivo(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",3400,LocalDate.of(2022,12,01)));
        objetivos.add(new Objetivo(3,"Play 5","Comprar play 5 pra jogar bom de guerra",
                5200, LocalDate.of(2022,11,30),"Entreterimento",2,LocalDate.of(2023,02,27)));
        when(objetivoRepository.findByUsuarioIdOrderByCodigo(1)).thenReturn(objetivos);

        ResponseEntity response = objetivoController.deletarObjetivoPilha(1);
        Objetivo objetivoDeletado = objetivoController.deletarObjetivoPilha(1).getBody();

        assertEquals(true,response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        verify(objetivoRepository, times(2)).deleteById(anyInt());
        assertEquals("Play 5",objetivoDeletado.getNome());
        assertEquals("Comprar play 5 pra jogar bom de guerra",objetivoDeletado.getDescricao());
        assertEquals(true,objetivos.get(2).equals(objetivoDeletado));
    }

    @Test
    @DisplayName("Deletar objetivo pilha retorna 404 quando não ha objetivos")
    void deletarObjetivoPilhaRetorna404QuandoNaoHaObjetivos() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(objetivoRepository.countByUsuarioId(1)).thenReturn(0);

        ResponseEntity response = objetivoController.deletarObjetivoPilha(1);

        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
        verify(objetivoRepository, times(0)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Deletar objetivo pilha retorna 404 quando não ha usuario")
    void deletarObjetivoPilhaRetorna404QuandoNaoHaUsuario() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        when(objetivoRepository.countByUsuarioId(1)).thenReturn(3);

        ResponseEntity response = objetivoController.deletarObjetivoPilha(1);

        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
        verify(objetivoRepository, times(0)).deleteById(anyInt());
    }


}
//package com.projetopi.loginlogoff.financas.receita;
//
//import com.projetopi.loginlogoff.usuario.Usuario;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import com.projetopi.loginlogoff.usuario.UsuarioRepository;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class ReceitaControllerTest {
//
//    @Autowired
//    private ReceitaController receitaController;
//
//    @MockBean
//    private ReceitaRepository receitaRepository;
//
//    @MockBean
//    private UsuarioRepository usuarioRepository;
//
//    @Test
//    @DisplayName("Nao listar receitas quando usuario não encontrado")
//    void retornar404QuandoUsuarioNaoForEncontrado() {
//        when(usuarioRepository.existsById(1)).thenReturn(false);
//        ResponseEntity response = receitaController.listarTodasReceitasDoUsuario(1);
//        List<Receita> listaReceitas = receitaController.listarTodasReceitasDoUsuario(1).getBody();
//
//        assertEquals(404,response.getStatusCodeValue());
//        assertNull(listaReceitas);
//    }
//
//    @Test
//    @DisplayName("Retornar lista vazia quando usuario nao possui receita")
//    void retornarListaVaziaQuandoUsuarioNaoTemReceitas() {
//        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
//        List<Receita> receita = new ArrayList<>();
//        when(receitaRepository.findByUsuarioIdOrderByData(anyInt())).thenReturn(receita);
//        ResponseEntity response = receitaController.listarTodasReceitasDoUsuario(1);
//        List<Receita> listaReceitas = receitaController.listarTodasReceitasDoUsuario(1).getBody();
//
//        assertEquals(204,response.getStatusCodeValue());
//        assertNull(listaReceitas);
//    }
//
//    @Test
//    @DisplayName("Listar todas receitas do usuário quando usuario existe e tem receitas")
//    void listarTodasReceitasDoUsuarioQuandoUsuarioExistsByEtrueEPossuiReceitas() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        List<Receita> receitas = new ArrayList<>();
//        receitas.add(new Receita(1,"Venda de camisa da seleção","vendas na minha lojinha",
//                960, LocalDate.of(2022,11,22),"Venda",false,0));
//        receitas.add(new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                3600, LocalDate.of(2022,11,28),"Trabalho",true,12));
//        receitas.add(new Receita(3,"Dinheiro do Marquinhos","Dinheiro que o marquinhos estava me devendo",
//                236.50, LocalDate.of(2022,11,30),"Informal",false,0));
//        when(receitaRepository.findByUsuarioIdOrderByData(1)).thenReturn(receitas);
//
//        ResponseEntity response = receitaController.listarTodasReceitasDoUsuario(1);
//        List<Receita> listaReceitas = receitaController.listarTodasReceitasDoUsuario(1).getBody();
//
//        assertNotNull(response.hasBody());
//        assertEquals(200,response.getStatusCodeValue());
//        assertEquals(receitas,listaReceitas);
//        assertEquals(3,listaReceitas.size());
//    }
//
//    @Test
//    @DisplayName("Buscar receita do usuario quando usuario existe e há receita")
//    void buscarDespesaDoUsuarioQuandoUsuarioExisteEDespesaExiste() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        when(receitaRepository.existsById(2)).thenReturn(true);
//
//        Receita receitaUsuario = new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                3600, LocalDate.of(2022,11,28),"Trabalho",true,12);
//
//        when(receitaRepository.findByUsuarioIdAndCodigo(1,2)).thenReturn(Optional.of(receitaUsuario));
//
//        ResponseEntity response = receitaController.buscarReceitaDoUsuario(1,2);
//        Receita receita = receitaController.buscarReceitaDoUsuario(1,2).getBody();
//
//        assertNotNull(response.hasBody());
//        assertEquals(200,response.getStatusCodeValue());
//        assertEquals(2,receita.getCodigo());
//        assertEquals("Salário do meu emprego formal",receita.getNome());
//        assertEquals("Salário mensal",receita.getDescricao());
//    }
//
//    @Test
//    @DisplayName("Buscar receita do usuario quando usuario não existe e há receita")
//    void buscarReceitaDoUsuarioRetorna404QuandoUsuarioNaoExiste() {
//        when(usuarioRepository.existsById(1)).thenReturn(false);
//        when(receitaRepository.existsById(2)).thenReturn(true);
//
//        ResponseEntity response = receitaController.buscarReceitaDoUsuario(1,2);
//        Receita receita = receitaController.buscarReceitaDoUsuario(1,2).getBody();
//
//        assertNotNull(response.hasBody());
//        assertNull(receita);
//        assertEquals(404,response.getStatusCodeValue());
//
//    }
//
//    @Test
//    @DisplayName("Buscar receita do usuario quando usuario existe e não há a receita")
//    void buscarReceitaDoUsuarioRetorna404QuandoReceitaNaoExiste() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        when(receitaRepository.existsById(2)).thenReturn(false);
//
//        ResponseEntity response = receitaController.buscarReceitaDoUsuario(1,2);
//        Receita receita = receitaController.buscarReceitaDoUsuario(1,2).getBody();
//
//        assertNotNull(response.hasBody());
//        assertNull(receita);
//        assertEquals(404,response.getStatusCodeValue());
//
//    }
//
//    @Test
//    @DisplayName("Criar receita do usuario quando usuario existir")
//    void criarReceitaQuandoUsuarioExistir() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        Usuario usuario = new Usuario();
//        usuario.setIdUsuario(1);
//        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
//        Receita receita = new Receita(1,"Venda de camisa da seleção","vendas na minha lojinha",
//                960, LocalDate.of(2022,11,22),"Venda",false,0);
//        when(receitaRepository.save(receita)).thenReturn(receita);
//
//        ResponseEntity response = receitaController.criarReceita(1,receita);
//        Receita receitaCriada = receitaController.criarReceita(1,receita).getBody();
//        assertNotNull(response.hasBody());
//        assertEquals(1,receitaCriada.getCodigo());
//        assertEquals(201,response.getStatusCodeValue());
//        assertEquals("Venda de camisa da seleção",receitaCriada.getNome());
//        assertEquals("vendas na minha lojinha",receitaCriada.getDescricao());
//    }
//
//    @Test
//    @DisplayName("Não criar receita quando usuario não existir")
//    void naoCriarReceitaQuandoUsuarioNaoExistir() {
//        when(usuarioRepository.existsById(1)).thenReturn(false);
//        Usuario usuario = new Usuario();
//        usuario.setIdUsuario(1);
//        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
//        Receita receita = new Receita(1,"Venda de camisa da seleção","vendas na minha lojinha",
//                960, LocalDate.of(2022,11,22),"Venda",false,0);
//        when(receitaRepository.save(receita)).thenReturn(receita);
//
//        ResponseEntity response = receitaController.criarReceita(1,receita);
//        assertNotNull(response.hasBody());
//        assertEquals(404,response.getStatusCodeValue());
//    }
//
//    @Test
//    @DisplayName("Atualizar receita quando usuario e receita existir")
//    void atualizaReceitaQuandoUsuarioEReceitaExistem() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        when(receitaRepository.existsById(2)).thenReturn(true);
//        List<Receita> receitas = new ArrayList<>();
//        receitas.add(new Receita(1,"Venda de camisa da seleção","vendas na minha lojinha",
//                960, LocalDate.of(2022,11,22),"Venda",false,0));
//        receitas.add(new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                3600, LocalDate.of(2022,11,28),"Trabalho",true,12));
//        Usuario usuario = new Usuario();
//        usuario.setIdUsuario(1);
//        receitas.get(0).setUsuario(usuario);
//        receitas.get(1).setUsuario(usuario);
//        Receita receitaAtualizada = new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                4500, LocalDate.of(2022,11,28),"Trabalho",true,12);
//        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
//        when(receitaRepository.save(any())).thenReturn(receitaAtualizada);
//
//        ResponseEntity response = receitaController.atualizarReceita(1,2,receitaAtualizada);
//        Receita novaReceita = receitaController.atualizarReceita(1,2,receitaAtualizada).getBody();
//
//        assertNotNull(response.hasBody());
//        assertEquals(200,response.getStatusCodeValue());
//        assertEquals(2,novaReceita.getCodigo());
//        assertEquals("Salário do meu emprego formal",novaReceita.getNome());
//        assertEquals(4500,novaReceita.getValor());
//        assertEquals(false,novaReceita.equals(receitas.get(1)));
//    }
//
//
//    @Test
//    @DisplayName("Não atualiza quando usuario não existir")
//    void naoAtualizaQuandoUsuarioNaoExistir() {
//        when(usuarioRepository.existsById(1)).thenReturn(false);
//        Receita receita = new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                4500, LocalDate.of(2022,11,28),"Trabalho",true,12);
//
//        ResponseEntity response = receitaController.atualizarReceita(1,2,receita);
//        assertNotNull(response.hasBody());
//        assertEquals(404,response.getStatusCodeValue());
//    }
//    @Test
//    @DisplayName("Não atualiza quando receita não existir")
//    void naoAtualizaQuandoReceitaNaoExistir() {
//        when(receitaRepository.existsById(anyInt())).thenReturn(false);
//        Receita receita = new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                4500, LocalDate.of(2022,11,28),"Trabalho",true,12);
//        ResponseEntity response = receitaController.atualizarReceita(1,2,receita);
//        assertNotNull(response.hasBody());
//        assertEquals(404,response.getStatusCodeValue());
//    }
//
//    @Test
//    @DisplayName("Deleta receita quando usuario e receita existem")
//    void deletaReceitaQuandoUsuarioEReceitaExistem() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        when(receitaRepository.existsById(2)).thenReturn(true);
//        Receita receita = new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                4500, LocalDate.of(2022,11,28),"Trabalho",true,12);
//        when(receitaRepository.findById(2)).thenReturn(Optional.of(receita));
//
//        ResponseEntity response = receitaController.deletarReceita(1,2);
//        Receita receitaDeletada = receitaController.deletarReceita(1,2).getBody();
//
//        verify(receitaRepository, times(2)).deleteById(2);
//        assertEquals(true,response.hasBody());
//        assertEquals(200,response.getStatusCodeValue());
//        assertEquals(2,receitaDeletada.getCodigo());
//        assertEquals("Salário do meu emprego formal",receitaDeletada.getNome());
//        assertEquals("Salário mensal",receitaDeletada.getDescricao());
//        assertEquals(true,receita.equals(receitaDeletada));
//    }
//
//    @Test
//    @DisplayName("Não deleta receita quando usuario nao existe")
//    void naoDeletaReceitaQuandoUsuarioNaoExistir() {
//        when(usuarioRepository.existsById(1)).thenReturn(false);
//
//        ResponseEntity response = receitaController.deletarReceita(1,2);
//
//        verify(receitaRepository, times(0)).deleteById(anyInt());
//        assertEquals(false,response.hasBody());
//        assertEquals(404,response.getStatusCodeValue());
//    }
//
//    @Test
//    @DisplayName("Não deleta receita quando receita nao existir")
//    void naoDeletaReceitaQuandoReceitaNaoExistir() {
//        when(receitaRepository.existsById(2)).thenReturn(false);
//
//        ResponseEntity response = receitaController.deletarReceita(1,2);
//
//        verify(receitaRepository, times(0)).deleteById(anyInt());
//        assertEquals(false,response.hasBody());
//        assertEquals(404,response.getStatusCodeValue());
//    }
//
//    @Test
//    @DisplayName("Deletar todas receitas do usuario")
//    void deletarTodasReceitas() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        List<Receita> receitas = new ArrayList<>();
//        receitas.add(new Receita(1,"Venda de camisa da seleção","vendas na minha lojinha",
//                960, LocalDate.of(2022,11,22),"Venda",false,0));
//        receitas.add(new Receita(2,"Salário do meu emprego formal","Salário mensal",
//                3600, LocalDate.of(2022,11,28),"Trabalho",true,12));
//        receitas.add(new Receita(3,"Dinheiro do Marquinhos","Dinheiro que o marquinhos estava me devendo",
//                236.50, LocalDate.of(2022,11,30),"Informal",false,0));
//        when(receitaRepository.findByUsuarioIdOrderByData(1)).thenReturn(receitas);
//
//        ResponseEntity response = receitaController.deletarTodasReceitas(1);
//        List<Receita> receitasDeletadas = receitaController.deletarTodasReceitas(1).getBody();
//
//        assertEquals(true,response.hasBody());
//        assertEquals(200,response.getStatusCodeValue());
//        assertEquals(3,receitasDeletadas.size());
//        verify(receitaRepository, times(6)).deleteById(anyInt());
//    }
//
//    @Test
//    @DisplayName("Deletar todas receitas do usuario retorna vazio caso receita nao tenha despesa")
//    void deletarTodasDespesasRetonaVazioQuandoUsuarioNaoTemDespesa() {
//        when(usuarioRepository.existsById(1)).thenReturn(true);
//        List<Receita> receitas = new ArrayList<>();
//        when(receitaRepository.findByUsuarioIdOrderByData(1)).thenReturn(receitas);
//
//        ResponseEntity response = receitaController.deletarTodasReceitas(1);
//
//        assertEquals(false,response.hasBody());
//        assertEquals(202,response.getStatusCodeValue());
//        verify(receitaRepository, times(0)).deleteById(anyInt());
//    }
//
//    @Test
//    @DisplayName("Deletar todas receitas do usuario retorna 404 quando usuario nao existir")
//    void deletarTodasReceitasRetona404QuandoUsuarioNaoExistir() {
//        when(usuarioRepository.existsById(1)).thenReturn(false);
//
//        ResponseEntity response = receitaController.deletarTodasReceitas(1);
//
//        assertEquals(false,response.hasBody());
//        assertEquals(404,response.getStatusCodeValue());
//        verify(receitaRepository, times(0)).deleteById(anyInt());
//    }
//
//}
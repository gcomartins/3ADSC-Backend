package com.projetopi.loginlogoff.financas.despesa;


import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.despesa.DespesaController;
import com.projetopi.loginlogoff.financas.despesa.DespesaRepository;
import com.projetopi.loginlogoff.financas.despesa.dto.DespesaDto;
import com.projetopi.loginlogoff.usuario.Usuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DespesaControllerTest {
    @Autowired
    private DespesaController despesaController;

    @MockBean
    private DespesaRepository despesaRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Não listar despesas quando usuario não encontrado")
    void retornar404QuandoUsuarioNaoForEncontrado() {
        when(usuarioRepository.existsById(anyInt())).thenReturn(false);
        ResponseEntity response = despesaController.listarTodasDespesasDoUsuario(1);
        List<Despesa> listaDespesas = despesaController.listarTodasDespesasDoUsuario(1).getBody();

        assertEquals(404,response.getStatusCodeValue());
        assertNull(listaDespesas);
    }

    @Test
    @DisplayName("Retornar lista vazia quando usuario nao possui despesa")
    void retornarListaVaziaQuandoUsuarioNaoTemDespesas() {
        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        List<Despesa> despesas = new ArrayList<>();
        when(despesaRepository.findByUsuarioIdOrderByData(anyInt())).thenReturn(despesas);
        ResponseEntity response = despesaController.listarTodasDespesasDoUsuario(1);
        List<Despesa> listaDespesas = despesaController.listarTodasDespesasDoUsuario(1).getBody();

        assertEquals(204,response.getStatusCodeValue());
        assertNull(listaDespesas);
    }
    @Test
    @DisplayName("Listar todas despesas do usuário quando usuario existe e tem despesas")
    void listarTodasDespesasDoUsuarioQuandoUsuarioExistsByEtrueEPossuiDespesas() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        List<Despesa> despesas = new ArrayList<>();
        despesas.add(new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",false,12));
        despesas.add(new Despesa(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",false,12));
        despesas.add(new Despesa(3,"Compra do mês","Compra do mês",
                640.99, LocalDate.of(2022,11,30),"Alimentação",true,0));
        when(despesaRepository.findByUsuarioIdOrderByData(1)).thenReturn(despesas);

        ResponseEntity response = despesaController.listarTodasDespesasDoUsuario(1);
        List<Despesa> listaDespesas = despesaController.listarTodasDespesasDoUsuario(1).getBody();

        assertNotNull(response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(despesas,listaDespesas);
        assertEquals(3,listaDespesas.size());
    }

    @Test
    @DisplayName("Buscar despesa do usuario quando usuario existe e há despesa")
    void buscarDespesaDoUsuarioQuandoUsuarioExisteEDespesaExiste() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(despesaRepository.existsById(2)).thenReturn(true);

        List<Despesa> despesas = new ArrayList<>();
        despesas.add(new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",false,12));
        despesas.add(new Despesa(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",false,12));
        despesas.add(new Despesa(3,"Compra do mês","Compra do mês",
                640.99, LocalDate.of(2022,11,30),"Alimentação",true,0));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        despesas.get(0).setUsuario(usuario);
        despesas.get(1).setUsuario(usuario);
        despesas.get(2).setUsuario(usuario);
        when(despesaRepository.findByUsuarioIdAndCodigo(1,2)).thenReturn(despesas.get(1));

        ResponseEntity response = despesaController.buscarDespesaDoUsuario(1,2);
        Despesa despesa = despesaController.buscarDespesaDoUsuario(1,2).getBody();

        assertNotNull(response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(2,despesa.getCodigo());
        assertEquals("Copa do mundo dia 2 só estádio",despesa.getNome());
        assertEquals("Ver o menino Ney",despesa.getDescricao());

    }

    @Test
    @DisplayName("Buscar despesa do usuario quando usuario não existe e há a despesa")
    void buscarDespesaDoUsuarioRetorna404QuandoUsuarioNaoExiste() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        when(despesaRepository.existsById(2)).thenReturn(true);

        ResponseEntity response = despesaController.buscarDespesaDoUsuario(1,2);
        Despesa despesa = despesaController.buscarDespesaDoUsuario(1,2).getBody();

        assertNotNull(response.hasBody());
        assertNull(despesa);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    @DisplayName("Buscar despesa do usuario quando usuario existe e não há despesa")
    void buscarDespesaDoUsuarioRetorna404QuandoDespesaNaoExiste() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(despesaRepository.existsById(2)).thenReturn(false);

        ResponseEntity response = despesaController.buscarDespesaDoUsuario(1,2);
        Despesa despesa = despesaController.buscarDespesaDoUsuario(1,2).getBody();

        assertNotNull(response.hasBody());
        assertNull(despesa);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    @DisplayName("Criar despesa do usuario quando usuario existir")
    void criarDespesaQuandoUsuarioExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        Despesa despesa = new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2100,11,22),"Entreterimento",false,12);
        when(despesaRepository.save(any())).thenReturn(despesa);

        ResponseEntity response = despesaController.criarDespesa(1,despesa);
        Despesa despesaCriada = despesaController.criarDespesa(1,despesa).getBody();
        assertNotNull(response.hasBody());
        assertEquals(1,despesaCriada.getCodigo());
        assertEquals(201,response.getStatusCodeValue());
        assertEquals("Copa do mundo",despesaCriada.getNome());
        assertEquals("Gastei tudo pra ver um jogo de 1 hora sem cerveja",despesaCriada.getDescricao());
    }

    @Test
    @DisplayName("Não criar despesa quando usuario não existir")
    void naoCriarDespesaQuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        Despesa despesa = new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500,LocalDate.of(2100,11,22),"Entreterimento",false,12);
        when(despesaRepository.save(any())).thenReturn(despesa);

        ResponseEntity response = despesaController.criarDespesa(1,despesa);
        assertNotNull(response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Atualizar despesa quando usuario e despesa existir")
    void atualizaDespesaQuandoUsuarioEDespesaExistem() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(despesaRepository.existsById(2)).thenReturn(true);
        List<Despesa> despesas = new ArrayList<>();
        despesas.add(new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",false,12));
        despesas.add(new Despesa(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",false,12));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        despesas.get(0).setUsuario(usuario);
        despesas.get(1).setUsuario(usuario);
        Despesa despesaAtualizada = new Despesa(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                1200, LocalDate.of(2022,11,28),"Entreterimento",false,7);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(despesaRepository.save(any())).thenReturn(despesaAtualizada);

        ResponseEntity response = despesaController.atualizarDespesa(1,2,despesaAtualizada);
        Despesa novaDespesa = despesaController.atualizarDespesa(1,2,despesaAtualizada).getBody();

        assertNotNull(response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(2,novaDespesa.getCodigo());
        assertEquals("Copa do mundo dia 2 só estádio",novaDespesa.getNome());
        assertEquals("Ver o menino Ney",novaDespesa.getDescricao());
        assertEquals(false,despesaAtualizada.equals(despesas.get(1)));
    }


    @Test
    @DisplayName("Não atualiza quando usuario não existir")
    void naoAtualizaQuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        Despesa despesa = new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2100,11,22),"Entreterimento",false,12);

        ResponseEntity response = despesaController.atualizarDespesa(1,2,despesa);
        assertNotNull(response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }
    @Test
    @DisplayName("Não atualiza quando despesa não existir")
    void naoAtualizaQuandoDespesaNaoExistir() {
        when(despesaRepository.existsById(anyInt())).thenReturn(false);
        Despesa despesa = new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2100,11,22),"Entreterimento",false,12);

        ResponseEntity response = despesaController.atualizarDespesa(1,2,despesa);
        assertNotNull(response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }



    @Test
    @DisplayName("Deleta despesa quando usuario e despesa existem")
    void deletaDespesaQuandoUsuarioEDespesaExistem() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(despesaRepository.existsById(2)).thenReturn(true);
        Despesa despesa = new Despesa(2,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2100,11,22),"Entreterimento",false,12);
        when(despesaRepository.findById(2)).thenReturn(Optional.of(despesa));

        ResponseEntity response = despesaController.deletarDespesa(1,2);
        Despesa despesaDeletada = despesaController.deletarDespesa(1,2).getBody();

        verify(despesaRepository, times(2)).deleteById(2);
        assertEquals(true,response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(2,despesaDeletada.getCodigo());
        assertEquals("Copa do mundo",despesaDeletada.getNome());
        assertEquals("Gastei tudo pra ver um jogo de 1 hora sem cerveja",despesaDeletada.getDescricao());
        assertEquals(true,despesa.equals(despesaDeletada));
    }

    @Test
    @DisplayName("Não deleta despesa quando usuario nao existe")
    void naoDeletaDespesaQuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);

        ResponseEntity response = despesaController.deletarDespesa(1,2);

        verify(despesaRepository, times(0)).deleteById(anyInt());
        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Não deleta despesa quando despesa nao existir")
    void naoDeletaDespesaQuandoDespesaNaoExistir() {
        when(despesaRepository.existsById(2)).thenReturn(false);

        ResponseEntity response = despesaController.deletarDespesa(1,2);

        verify(despesaRepository, times(0)).deleteById(anyInt());
        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }


    @Test
    @DisplayName("Deletar todas despesas do usuario")
    void deletarTodasDespesas() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        List<Despesa> despesas = new ArrayList<>();

        despesas.add(new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",false,12));
        despesas.add(new Despesa(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",false,12));
        despesas.add(new Despesa(3,"Compra do mês","Compra do mês",
                640.99, LocalDate.of(2022,11,30),"Alimentação",true,0));
        when(despesaRepository.findByUsuarioIdOrderByData(1)).thenReturn(despesas);

        ResponseEntity response = despesaController.deletarTodasDespesas(1);
        List<Despesa> despesasDeletadas = despesaController.deletarTodasDespesas(1).getBody();

        assertEquals(true,response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(3,despesasDeletadas.size());
        verify(despesaRepository, times(6)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Deletar todas despesas do usuario retorna vazio caso usuario nao tenha despesa")
    void deletarTodasDespesasRetonaVazioQuandoUsuarioNaoTemDespesa() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        List<Despesa> despesas = new ArrayList<>();
        when(despesaRepository.findAll()).thenReturn(despesas);

        despesas.add(new Despesa(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500, LocalDate.of(2022,11,22),"Entreterimento",false,12));
        despesas.add(new Despesa(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600, LocalDate.of(2022,11,28),"Entreterimento",false,12));
        despesas.add(new Despesa(3,"Compra do mês","Compra do mês",
                640.99, LocalDate.of(2022,11,30),"Alimentação",true,0));

        ResponseEntity response = despesaController.deletarTodasDespesas(1);

        assertEquals(false,response.hasBody());
        assertEquals(202,response.getStatusCodeValue());
        verify(despesaRepository, times(0)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Deletar todas despesas do usuario retorna 404 quando usuario nao existir")
    void deletarTodasDespesasRetona404QuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        List<Despesa> despesas = new ArrayList<>();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(2);

        ResponseEntity response = despesaController.deletarTodasDespesas(1);

        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
        verify(despesaRepository, times(0)).deleteById(anyInt());
    }

    @Test
    void listarTodasDespesasDtoDoUsuario() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        List<DespesaDto> despesas = new ArrayList<>();
        despesas.add(new DespesaDto(1,"Copa do mundo","Gastei tudo pra ver um jogo de 1 hora sem cerveja",
                12500.0));
        despesas.add(new DespesaDto(2,"Copa do mundo dia 2 só estádio","Ver o menino Ney",
                3600.0));
        when(despesaRepository.getDespesaDto(1)).thenReturn(despesas);

        ResponseEntity response = despesaController.listarTodasDespesasDtoDoUsuario(1);
        List<DespesaDto> listaDespesaDto = despesaController.listarTodasDespesasDtoDoUsuario(1).getBody();

        assertNotNull(response.hasBody());
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(despesas,listaDespesaDto);
        assertEquals(2,listaDespesaDto.size());
    }

    @Test
    void listarTodasDespesasDtoDoUsuarioQuandoNaoTiverDespesas() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        List<DespesaDto> despesas = new ArrayList<>();
        when(despesaRepository.getDespesaDto(1)).thenReturn(despesas);

        ResponseEntity response = despesaController.listarTodasDespesasDtoDoUsuario(1);
        List<DespesaDto> listaDespesaDto = despesaController.listarTodasDespesasDtoDoUsuario(1).getBody();

        assertNotNull(response.hasBody());
        assertEquals(204,response.getStatusCodeValue());
        assertNull(listaDespesaDto);
    }

    @Test
    void listarTodasDespesasDtoDoUsuarioRetornar404QuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsById(1)).thenReturn(false);

        ResponseEntity response = despesaController.listarTodasDespesasDtoDoUsuario(1);

        assertEquals(false,response.hasBody());
        assertEquals(404,response.getStatusCodeValue());
    }
}
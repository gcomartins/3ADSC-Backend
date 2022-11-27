package com.projetopi.loginlogoff.financas.despesa;

import com.projetopi.loginlogoff.financas.RelatorioMensal;
import com.projetopi.loginlogoff.financas.despesa.dto.DespesaDto;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.receita.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.DoubleBuffer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Integer> {
    List<Despesa> findByUsuarioIdOrderByData(int idUsuario);
    Despesa findByUsuarioIdAndCodigo(int idUsuario, int codigo);

    int countByUsuarioId(int idUsuario);

    @Query("SELECT" +
            " new com.projetopi.loginlogoff.financas.despesa.dto.DespesaDto(d.codigo, d.nome, d.descricao, d.valor)" +
            " FROM Despesa d where usuario_id = ?1")
    List<DespesaDto> getDespesaDto(int fkUsuario);

    List<Despesa> findByUsuarioIdAndDataBetween(int idUsuario, LocalDate dataInico, LocalDate dataFim);



    @Query("SELECT " +
            "new com.projetopi.loginlogoff.financas.RelatorioMensal(SUM(d.valor), month(d.data), year(d.data))" +
            " from Despesa d where usuario_id = ?1 group by year(d.data), month(d.data)")
    List<RelatorioMensal> getValorAgrupadoPorData(int idUsuario);

}

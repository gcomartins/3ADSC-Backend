package com.projetopi.loginlogoff.financas.despesa;

import com.projetopi.loginlogoff.financas.despesa.dto.DespesaDto;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Integer> {
    List<Despesa> findByUsuarioIdOrderByData(int idUsuario);
    int countByUsuarioId(int idUsuario);

    @Query("SELECT" +
            " new com.projetopi.loginlogoff.financas.despesa.dto.DespesaDto(d.codigo, d.nome, d.descricao, d.valor)" +
            " FROM Despesa d where usuario_id = ?1")
    List<DespesaDto> getDespesaDto(int fkUsuario);
}

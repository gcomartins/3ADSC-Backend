package com.projetopi.loginlogoff.financas.receita;

import com.projetopi.loginlogoff.financas.RelatorioMensal;
import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Integer> {
    List<Receita> findByUsuarioIdOrderByData(int idUsuario);
    int countByUsuarioId(int idUsuario);
    Optional<Receita> findByUsuarioIdAndCodigo(int idUsuario, int codigo);
    List<Receita> findByUsuarioIdAndDataBetween(int idUsuario, LocalDate dataInico, LocalDate dataFim);

    @Query("SELECT " +
            "r.data from Receita r where usuario_id = ?1")
    List<LocalDate> getData(int idUsuario);

    @Query("SELECT " +
            "new com.projetopi.loginlogoff.financas.RelatorioMensal(SUM(r.valor), month(r.data), year(r.data))" +
            " from Receita r where usuario_id = ?1 group by year(r.data), month(r.data)")
    List<RelatorioMensal> getValorAgrupadoPorData(int idUsuario);

}

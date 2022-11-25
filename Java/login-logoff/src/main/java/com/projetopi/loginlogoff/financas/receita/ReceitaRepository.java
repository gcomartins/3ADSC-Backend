package com.projetopi.loginlogoff.financas.receita;

import com.projetopi.loginlogoff.financas.despesa.Despesa;
import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Integer> {
    List<Receita> findByUsuarioIdOrderByData(int idUsuario);
    int countByUsuarioId(int idUsuario);

    List<Receita> findByUsuarioIdAndDataBetween(int idUsuario, LocalDate dataInico, LocalDate dataFim);
}

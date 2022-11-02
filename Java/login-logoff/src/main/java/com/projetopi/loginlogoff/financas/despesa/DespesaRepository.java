package com.projetopi.loginlogoff.financas.despesa;

import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Integer> {
    List<Despesa> findByUsuarioIdOrderByData(int idUsuario);

    int countByUsuarioId(int idUsuario);

}

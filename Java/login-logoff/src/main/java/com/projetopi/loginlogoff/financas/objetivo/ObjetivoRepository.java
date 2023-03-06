package com.projetopi.loginlogoff.financas.objetivo;

import com.projetopi.loginlogoff.financas.receita.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ObjetivoRepository extends JpaRepository<Objetivo,Integer> {
    List<Objetivo> findByUsuarioIdOrderByData(int idUsuario);
    int countByUsuarioId(int idUsuario);

    List<Objetivo> findByUsuarioIdAndCodigoOrderByData(int usuarioId, int codigo);

    List<Objetivo> findByUsuarioIdOrderByCodigo(int usuarioId);



}

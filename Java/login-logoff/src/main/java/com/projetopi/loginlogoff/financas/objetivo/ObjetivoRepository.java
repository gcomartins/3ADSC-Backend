package com.projetopi.loginlogoff.financas.objetivo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjetivoRepository extends JpaRepository<Objetivo,Integer> {
    List<Objetivo> findByUsuarioIdOrderByData(int idUsuario);
    int countByUsuarioId(int idUsuario);

    List<Objetivo> findByUsuarioIdAndCodigoOrderByData(int usuarioId, int codigo);

<<<<<<< HEAD
=======
    List<Objetivo> findByUsuarioIdOrderByCodigo(int usuarioId);
>>>>>>> 89b8615d628eb0d40e6eb0e9d82963d43f175099
}

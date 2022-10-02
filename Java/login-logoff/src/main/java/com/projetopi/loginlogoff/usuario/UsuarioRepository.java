package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {


}

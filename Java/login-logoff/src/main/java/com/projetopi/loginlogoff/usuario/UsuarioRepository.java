package com.projetopi.loginlogoff.usuario;

import com.projetopi.loginlogoff.financas.objetivo.Objetivo;
import com.projetopi.loginlogoff.financas.receita.Receita;
import com.projetopi.loginlogoff.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import java.util.List;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Usuario findByEmailAndSenha(String email, String senha);
    Usuario findByEmail(String email);
    Usuario findByCpf(String cpf);
}

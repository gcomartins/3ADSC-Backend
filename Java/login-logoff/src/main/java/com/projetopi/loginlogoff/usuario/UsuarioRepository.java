package com.projetopi.loginlogoff.usuario;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Email;
import java.util.List;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Usuario findByEmailAndSenha(String email, String senha);
    Usuario findByEmail(String email);

    @Modifying
    @Transactional
    @Query(" update Usuario u set u.arquivoTxt = ?2 where u.id = ?1 ")
    void setArquivoTxt(Integer id, Byte[] arquivoTxt);

    @Query("select u.arquivoTxt from Usuario u where u.id = ?1")
    Byte[] getArquivoTxt(Integer idUsuario);

}

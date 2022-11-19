package com.projetopi.loginlogoff.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projetopi.loginlogoff.usuario.ControllerUsuario;
import com.projetopi.loginlogoff.usuario.UsuarioRepository;
@Service
public class ServiceUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;


    public  Boolean atualizarUsuario(String emailAntigo, String senhaAntiga,
                                                     Usuario usuarioAtualizado ) {
        Boolean isAtualizado = false;
        Usuario usuario = usuarioRepository.findByEmailAndSenha(emailAntigo, senhaAntiga);
        if (usuario == null) return isAtualizado;
        if (usuarioAtualizado.getSenha().equals(senhaAntiga) &&
                usuarioAtualizado.getEmail().equals(emailAntigo) &&
                usuarioAtualizado.getNome().equals(usuario.getNome()) ) {
            return isAtualizado;
        }
        System.out.println(usuario);
        usuario.setSenha(usuarioAtualizado.getSenha());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setNome(usuarioAtualizado.getNome());
        usuarioRepository.save(usuario);
        isAtualizado = true;
        return isAtualizado;
    }

}

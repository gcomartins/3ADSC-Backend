package com.projetopi.loginlogoff.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public class ServiceUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;


    public  ResponseEntity<Usuario> atualizarUsuario(String emailAntigo, String senhaAntiga,
                                                     Usuario usuarioAtualizado ) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(emailAntigo, senhaAntiga);
        if (usuario == null) return ResponseEntity.status(400).build();
        if (usuarioAtualizado.getSenha().equals(senhaAntiga) &&
                usuarioAtualizado.getEmail().equals(emailAntigo) &&
                usuarioAtualizado.getNome().equals(usuario.getNome()) ) {
            return ResponseEntity.status(400).build();
        }

        usuario.setSenha(usuarioAtualizado.getSenha());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setNome(usuarioAtualizado.getNome());
        usuarioRepository.save(usuario);
        return ResponseEntity.status(202).body(usuario);
    }

}

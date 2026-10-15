package com.example.GameVault.service;

import com.example.GameVault.model.Usuario;
import com.example.GameVault.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario obtenerUsuarioLogeado(){
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(1L);

        if(usuarioOpt.isPresent()){
            return usuarioOpt.get();
        }



        else {
            Usuario usuario = new Usuario();
            usuario.setNombre("Itson");
            usuario.setEmail("itson@itson.edu.mx");
            usuario.setContrasenia("10234");

            return usuarioRepository.save(usuario);

        }

    }
}

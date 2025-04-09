package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getUsuarioLogado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return (Usuario) usuarioRepository.findByEmail(username);
    }
}


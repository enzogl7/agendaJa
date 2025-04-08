package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public Usuario findByEmail(String email);
}

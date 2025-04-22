package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.PaginaNegocio;
import com.ogl.agendaJa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaginaNegocioRepository extends JpaRepository<PaginaNegocio, Integer> {
    PaginaNegocio findByUsuario(Usuario usuario);

    PaginaNegocio findBySlug(String slug);
}

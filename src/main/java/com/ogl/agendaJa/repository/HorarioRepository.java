package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.Horario;
import com.ogl.agendaJa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    Horario findAllByUsuario(Usuario usuario);
}
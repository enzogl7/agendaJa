package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.Servico;
import com.ogl.agendaJa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findAllByUsuario(Usuario usuario);

    Long countServicosByUsuario(Usuario usuario);
}

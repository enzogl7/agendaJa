package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findAllByPrestador(Usuario usuario);

    @Query(value = "SELECT COUNT(*) FROM agendamentos a WHERE a.prestador_id = :usuarioId AND a.status = :status", nativeQuery = true)
    Long countAgendamentosByStatusAndPrestador(@Param("usuarioId") Long usuarioId, @Param("status") String status);
}

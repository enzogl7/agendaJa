package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.model.Servico;
import com.ogl.agendaJa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findAllByPrestador(Usuario usuario);

    @Query(value = "SELECT COUNT(*) FROM agendamentos a WHERE a.prestador_id = :usuarioId AND a.status = :status", nativeQuery = true)
    Long countAgendamentosByStatusAndPrestador(@Param("usuarioId") Long usuarioId, @Param("status") String status);

    List<Agendamento> findAllByCliente(Usuario cliente);

    @Query("SELECT a FROM agendamentos a WHERE a.data BETWEEN :inicio AND :fim AND a.status IN ('CONFIRMADO', 'CONCLUIDO')")
    List<Agendamento> findAgendamentosDoMes(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    List<Agendamento> findAllByClienteCadastrado_Id(Long clienteCadastradoId);

    List<Agendamento> findAllByServico(Servico servico);
}

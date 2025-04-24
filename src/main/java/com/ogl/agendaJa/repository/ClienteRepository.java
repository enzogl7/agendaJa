package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAllByPrestadorId(Long prestadorId);

    Long countClientesByPrestador_Id(Long prestadorId);
}

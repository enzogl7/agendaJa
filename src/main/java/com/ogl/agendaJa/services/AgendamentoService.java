package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public void salvar(Agendamento agendamento) {
        agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> findAllByUsuario(Usuario usuario) {
        return agendamentoRepository.findAllByPrestador(usuario);
    }

    public Agendamento findById(Long id) {
        return agendamentoRepository.findById(id).get();
    }

    public void excluir(Agendamento agendamento) {
        agendamentoRepository.delete(agendamento);
    }

    public Long countAgendamentosPendentesPorUsuario(Usuario usuario) {
        return agendamentoRepository.countAgendamentosByStatusAndPrestador(usuario.getId(), "PENDENTE");
    }
}

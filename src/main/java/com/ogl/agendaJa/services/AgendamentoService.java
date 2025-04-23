package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.model.Horario;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<String> gerarHorariosDisponiveis(Horario horario) {
        List<String> horariosDisponiveis = new ArrayList<>();
        LocalTime inicioExpediente = LocalTime.parse(horario.getInicio_expediente());
        LocalTime fimExpediente = LocalTime.parse(horario.getFim_expediente());
        LocalTime inicioPausa = LocalTime.parse(horario.getInicio_pausa());
        LocalTime fimPausa = LocalTime.parse(horario.getFim_pausa());
        LocalTime atual = inicioExpediente;

        while (atual.isBefore(fimExpediente)) {
            boolean dentroDaPausa = !atual.isBefore(inicioPausa) && atual.isBefore(fimPausa);

            if (!dentroDaPausa) {
                horariosDisponiveis.add(atual.toString());
            }

            atual = atual.plusMinutes(30);
        }
        return horariosDisponiveis;
    }
}

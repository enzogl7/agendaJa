package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.model.Horario;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public List<Agendamento> findAllByCliente(Usuario usuario) {
        return agendamentoRepository.findAllByCliente(usuario);
    }

    public String getReceitaMensal() {
        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        LocalDate fim = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<Agendamento> agendamentosMes = agendamentoRepository.findAgendamentosDoMes(inicio, fim);

        BigDecimal total = agendamentosMes.stream()
                .map(agendamento -> agendamento.getServico().getPreco())
                .map(valor -> valor.replace("R$", "").replace(",", ".").replaceAll("\\s+", "").replaceAll("[^\\d.]", ""))
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return "R$ " + String.format(Locale.forLanguageTag("pt-BR"), "%,.2f", total);
    }

}

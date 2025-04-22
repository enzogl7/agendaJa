package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.model.AgendamentoDTO;
import com.ogl.agendaJa.model.EdicaoAgendamentoDTO;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.services.AgendamentoService;
import com.ogl.agendaJa.services.ServicoService;
import com.ogl.agendaJa.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Controller
public class AgendamentoController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private AgendamentoService agendamentoService;

    @RequestMapping("/prestador/agendamentos")
    public String agendamentosPrestador(Model model) {
        List<Agendamento> agendamentos = agendamentoService.findAllByUsuario(usuarioService.getUsuarioLogado());
        agendamentos.sort(Comparator.comparing(Agendamento::getData));
        List<String> datasAgendadas = agendamentos.stream().map(a -> a.getData().format(DateTimeFormatter.ISO_LOCAL_DATE)).toList();
        long agendamentosHoje = agendamentos.stream().filter(a -> a.getData().equals(LocalDate.now())).count();

        model.addAttribute("servicos", servicoService.findAllByUsuario(usuarioService.getUsuarioLogado()));
        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("datasAgendadas", datasAgendadas);
        model.addAttribute("agendamentosHoje", agendamentosHoje);
        model.addAttribute("hoje", LocalDate.now());
        model.addAttribute("agendamentosPendentes", agendamentoService.countAgendamentosPendentesPorUsuario(usuarioService.getUsuarioLogado()));
        return "prestador/agendamentos";
    }

    @PostMapping("/prestador/salvaragendamento")
    public ResponseEntity salvarAgendamento(@RequestBody AgendamentoDTO agendamentoDTO) {
        try {
            Usuario usuario = usuarioService.getUsuarioLogado();
            if (usuario.getPlanoSelecionado().equals("BASICO")) {
                List<Agendamento> agendamentos = agendamentoService.findAllByUsuario(usuario);
                if (agendamentos.size() == 10) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }
            Agendamento agendamento = new Agendamento();
            agendamento.setServico(servicoService.findById(Long.valueOf(agendamentoDTO.servico())));
            agendamento.setData(LocalDate.parse(agendamentoDTO.data()));
            agendamento.setHorario(agendamentoDTO.horario());
            agendamento.setPrestador(usuarioService.getUsuarioLogado());
            agendamento.setStatus(agendamentoDTO.status());
            agendamentoService.salvar(agendamento);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/excluiragendamento")
    public ResponseEntity excluirAgendamento(@RequestParam("idAgendamento") String idAgendamento) {
        try {
            Agendamento agendamento = agendamentoService.findById(Long.valueOf(idAgendamento));
            agendamentoService.excluir(agendamento);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/editaragendamento")
    public ResponseEntity editarAgendamento(@RequestBody EdicaoAgendamentoDTO edicaoAgendamento) {
        try {
            Agendamento agendamento = agendamentoService.findById(Long.valueOf(edicaoAgendamento.id()));
            agendamento.setServico(servicoService.findById(Long.valueOf(edicaoAgendamento.servico())));
            agendamento.setData(LocalDate.parse(edicaoAgendamento.data()));
            agendamento.setHorario(edicaoAgendamento.horario());
            agendamento.setPrestador(usuarioService.getUsuarioLogado());
            agendamento.setStatus(edicaoAgendamento.status());
            agendamentoService.salvar(agendamento);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/confirmaragendamento")
    public ResponseEntity confirmarAgendamento(@RequestParam("idAgendamento")String idAgendamento) {
        try {
            Agendamento agendamento = agendamentoService.findById(Long.valueOf(idAgendamento));
            agendamento.setStatus("CONFIRMADO");
            agendamentoService.salvar(agendamento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/cancelaragendamento")
    public ResponseEntity cancelarAgendamento(@RequestParam("idAgendamento")String idAgendamento) {
        try {
            Agendamento agendamento = agendamentoService.findById(Long.valueOf(idAgendamento));
            agendamento.setStatus("CANCELADO"); // todo: criar um schedule para deletar esses agendamentos depois de algum tempo "CANCELADO"
            agendamentoService.salvar(agendamento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

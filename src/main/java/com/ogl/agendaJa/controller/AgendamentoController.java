package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.*;
import com.ogl.agendaJa.model.dto.AgendamentoClienteDTO;
import com.ogl.agendaJa.model.dto.AgendamentoDTO;
import com.ogl.agendaJa.model.dto.EdicaoAgendamentoDTO;
import com.ogl.agendaJa.services.*;
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
import java.util.Collections;
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
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private MailService mailService;

    @RequestMapping("/prestador/agendamentos")
    public String agendamentosPrestador(Model model) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        List<Agendamento> agendamentos = agendamentoService.findAllByUsuario(usuarioLogado);
        agendamentos.sort(Comparator.comparing((Agendamento a) -> a.getStatus().equals("PENDENTE") ? 0 : (a.getStatus().equals("CANCELADO") || a.getStatus().equals("CONCLUIDO")) ? 2 : 1).thenComparing(Agendamento::getData));
        List<String> datasAgendadas = agendamentos.stream().map(a -> a.getData().format(DateTimeFormatter.ISO_LOCAL_DATE)).toList();
        long agendamentosHoje = agendamentos.stream().filter(a -> a.getData().equals(LocalDate.now())).count();
        Horario horarios = horarioService.findAllByUsuario(usuarioLogado);
        List<String> horariosDisponiveis = horarios != null ? agendamentoService.gerarHorariosDisponiveis(horarios) : Collections.emptyList();

        model.addAttribute("servicos", servicoService.findAllAtivoByUsuario(usuarioLogado));
        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("datasAgendadas", datasAgendadas);
        model.addAttribute("agendamentosHoje", agendamentosHoje);
        model.addAttribute("hoje", LocalDate.now());
        model.addAttribute("agendamentosPendentes", agendamentoService.countAgendamentosPendentesPorUsuario(usuarioLogado));
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        model.addAttribute("clientes", clienteService.findAllByPrestador(usuarioLogado));
        return "prestador/agendamentos";
    }

    @RequestMapping("/cliente/agendamentos")
    public String agendamentosCliente(Model model) {
        List<Agendamento> agendamentos = agendamentoService.findAllByCliente(usuarioService.getUsuarioLogado());
        agendamentos.sort(
                Comparator.comparing((Agendamento a) -> {
                    String status = a.getStatus();
                    if (status.equals("CONFIRMADO")) return 0;
                    if (status.equals("PENDENTE")) return 1;
                    if (status.equals("CONCLUIDO")) return 2;
                    return 3;
                }).thenComparing(Agendamento::getData));
        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("hoje", LocalDate.now());
        return "cliente/agendamentos_cliente";
    }

    @PostMapping("/prestador/salvaragendamento")
    public ResponseEntity salvarAgendamento(@RequestBody AgendamentoDTO agendamentoDTO) {
        try {
            Agendamento agendamento = new Agendamento();
            Usuario usuario = usuarioService.getUsuarioLogado();
            if (usuario.getPlanoSelecionado().equals("BASICO")) {
                List<Agendamento> agendamentos = agendamentoService.findAllByUsuario(usuario);
                if (agendamentos.size() == 10) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }

            if (!agendamentoDTO.nome().isEmpty() && !agendamentoDTO.telefone().isEmpty()) {
                Cliente clienteNovo = new Cliente();
                clienteNovo.setNome(agendamentoDTO.nome());
                clienteNovo.setTelefone(agendamentoDTO.telefone());
                clienteNovo.setPrestador(usuarioService.getUsuarioLogado());
                clienteService.salvar(clienteNovo);
                agendamento.setClienteCadastrado(clienteNovo);
            } else {
                agendamento.setClienteCadastrado(clienteService.findById(Long.valueOf(agendamentoDTO.cliente())));
            }

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
            agendamento.setClienteCadastrado(clienteService.findById(Long.valueOf(edicaoAgendamento.cliente())));
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
            mailService.enviarEmailConfirmacaoAgendamento(agendamento);
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

    @PostMapping("/negocio/salvaragendamentocliente")
    public ResponseEntity salvarAgendamentoCliente(@RequestBody AgendamentoClienteDTO agendamentoDTO) {
        try {
            // verifica se o dono do negocio possui plano
            Usuario prestador = usuarioService.findById(Long.valueOf(agendamentoDTO.prestador()));
            Usuario cliente = usuarioService.getUsuarioLogado();
            List<Agendamento> agendamentosPrestador = agendamentoService.findAllByUsuario(prestador);
            if (agendamentosPrestador.size() >= 10 && prestador.getPlanoSelecionado().equals("BASICO")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // caso ok, salva o agendamento
            Agendamento agendamento = new Agendamento();
            agendamento.setServico(servicoService.findById(Long.valueOf(agendamentoDTO.servico())));
            agendamento.setData(LocalDate.parse(agendamentoDTO.data(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            agendamento.setHorario(agendamentoDTO.horario());
            agendamento.setPrestador(prestador);
            agendamento.setFormaPagamento(agendamentoDTO.pagamento());
            agendamento.setCliente(cliente);
            agendamento.setStatus("PENDENTE");
            agendamentoService.salvar(agendamento);
            // mailService.enviarEmailConfirmacaoAgendamento(cliente.getEmail(), cliente.getNome(), prestador);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/concluiragendamento")
    public ResponseEntity concluirAgendamento(@RequestParam("idAgendamento")String idAgendamento) {
        try {
            Agendamento agendamento = agendamentoService.findById(Long.valueOf(idAgendamento));
            agendamento.setStatus("CONCLUIDO");
            agendamentoService.salvar(agendamento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

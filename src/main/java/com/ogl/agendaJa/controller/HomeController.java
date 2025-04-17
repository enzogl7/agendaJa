package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.Agendamento;
import com.ogl.agendaJa.services.AgendamentoService;
import com.ogl.agendaJa.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping("/prestador/home")
    public String prestadorHome(Model model) {
        List<Agendamento> agendamentos = agendamentoService.findAllByUsuario(usuarioService.getUsuarioLogado());
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(4);
        agendamentos = agendamentos.stream()
                .filter(a -> !a.getData().isBefore(hoje) && !a.getData().isAfter(limite))
                .filter(a -> a.getStatus().equals("CONFIRMADO"))
                .collect(Collectors.toList());

        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("qtdeAgendamentosProximos", agendamentos.size());
        model.addAttribute("agendamentosPendentes", agendamentoService.countAgendamentosPendentesPorUsuario(usuarioService.getUsuarioLogado()));
        return "/prestador/home";
    }

    @GetMapping("/cliente/home")
    public String clienteHome() {
        return "/cliente/home_cliente";
    }
}

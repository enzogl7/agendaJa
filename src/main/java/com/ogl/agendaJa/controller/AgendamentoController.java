package com.ogl.agendaJa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AgendamentoController {

    @RequestMapping("/prestador/agendamentos")
    public String agendamentosPrestador(Model model) {
        return "prestador/agendamentos";
    }
}

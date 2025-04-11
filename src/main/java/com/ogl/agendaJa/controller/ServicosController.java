package com.ogl.agendaJa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServicosController {

    @RequestMapping("/prestador/servicos")
    public String servicosPrestador() {
        return "prestador/servicos";
    }
}

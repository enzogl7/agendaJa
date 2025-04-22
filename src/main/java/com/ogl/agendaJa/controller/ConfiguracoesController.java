package com.ogl.agendaJa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfiguracoesController {
    @GetMapping("/prestador/configuracoes")
    public String configuracoes(){
        return "/prestador/configuracoes";
    }
}

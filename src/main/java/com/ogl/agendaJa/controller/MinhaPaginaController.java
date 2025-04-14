package com.ogl.agendaJa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MinhaPaginaController {

    @GetMapping("/prestador/minhapagina")
    public String minhaPagina() {
        return "/prestador/minha_pagina";
    }
}

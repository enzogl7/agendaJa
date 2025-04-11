package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/prestador/home")
    public String prestadorHome() {
        return "/prestador/home";
    }

    @GetMapping("/cliente/home")
    public String clienteHome() {
        return "/cliente/home_cliente";
    }
}

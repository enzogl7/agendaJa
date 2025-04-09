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

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("usuario", usuarioService.getUsuarioLogado());
        return "/home/home";
    }
}

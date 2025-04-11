package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void adicionarUsuarioAoModel(Model model) {
        model.addAttribute("usuario", usuarioService.getUsuarioLogado());
    }
}

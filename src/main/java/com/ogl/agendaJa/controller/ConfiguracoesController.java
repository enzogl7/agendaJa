package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.Horario;
import com.ogl.agendaJa.model.dto.HorarioDTO;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.services.HorarioService;
import com.ogl.agendaJa.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConfiguracoesController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private HorarioService horarioService;

    private void preencherHorario(Horario horario, HorarioDTO dto) {
        horario.setInicio_expediente(dto.inicio_expediente());
        horario.setFim_expediente(dto.fim_expediente());
        horario.setInicio_pausa(dto.inicio_pausa());
        horario.setFim_pausa(dto.fim_pausa());
        horario.setDatasFolgas(dto.datasFolga());
        horario.setUsuario(usuarioService.getUsuarioLogado());
    }

    @GetMapping("/prestador/configuracoes")
    public String configuracoes(Model model){
        model.addAttribute("horarios", horarioService.findAllByUsuario(usuarioService.getUsuarioLogado()));
        return "/prestador/configuracoes";
    }

    @PostMapping("/prestador/salvarhorarios")
    public ResponseEntity salvarHorarios(@RequestBody HorarioDTO horarioDTO) {
        try {
            // edita caso o usuário já tenha configurado antes
            Horario horarioExistente = horarioService.findAllByUsuario(usuarioService.getUsuarioLogado());
            if (horarioExistente != null) {
                preencherHorario(horarioExistente, horarioDTO);
                horarioService.salvar(horarioExistente);
                return ResponseEntity.ok().build();
            }

            // salva um novo caso o usuario ainda nao tenha configurado seus horarios
            Horario horario = new Horario();
            preencherHorario(horario, horarioDTO);
            horarioService.salvar(horario);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/salvarpix")
    public ResponseEntity salvarPix(@RequestParam("chavePix") String chavePix) {
        try {
            Usuario usuario = usuarioService.getUsuarioLogado();
            usuario.setChavePix(chavePix);
            usuarioService.salvar(usuario);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

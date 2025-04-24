package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.dto.EdicaoServicoDTO;
import com.ogl.agendaJa.model.Servico;
import com.ogl.agendaJa.model.dto.ServicoDTO;
import com.ogl.agendaJa.services.ServicoService;
import com.ogl.agendaJa.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ServicosController {

    @Autowired
    private ServicoService servicoService;
    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping("/prestador/servicos")
    public String servicosPrestador(Model model) {
        model.addAttribute("servicos", servicoService.findAllByUsuario(usuarioService.getUsuarioLogado()));
        model.addAttribute("qtdeServicos", servicoService.qtdeServicosPorUsuario(usuarioService.getUsuarioLogado()));
        return "prestador/servicos";
    }

    @PostMapping("/prestador/salvarservico")
    public ResponseEntity salvarServico(@RequestBody ServicoDTO servicoDTO) {
        try {
            Servico servico = new Servico();
            servico.setNome(servicoDTO.nome());
            servico.setDescricao(servicoDTO.descricao());
            servico.setPreco(servicoDTO.preco());
            servico.setCategoria(servicoDTO.categoria());
            servico.setAtivo(true);
            servico.setUsuario(usuarioService.getUsuarioLogado());
            servicoService.salvar(servico);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/excluirservico")
    public ResponseEntity excluirServico(@RequestParam("idServico") String idServico) {
        try {
            Servico servico = servicoService.findById(Long.valueOf(idServico));
            servicoService.excluir(servico);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/prestador/editarservico")
    public ResponseEntity editarServico(@RequestBody EdicaoServicoDTO edicaoServico) {
        try {
            Servico servico = servicoService.findById(Long.valueOf(edicaoServico.id()));
            servico.setNome(edicaoServico.nome());
            servico.setDescricao(edicaoServico.descricao());
            servico.setPreco(edicaoServico.preco());
            servico.setCategoria(edicaoServico.categoria());
            servico.setAtivo(edicaoServico.ativo());
            servico.setUsuario(usuarioService.getUsuarioLogado());
            servicoService.salvar(servico);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

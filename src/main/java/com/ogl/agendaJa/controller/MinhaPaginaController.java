package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.*;
import com.ogl.agendaJa.model.dto.MinhaPaginaDTO;
import com.ogl.agendaJa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MinhaPaginaController {

    @Autowired
    private ServicoService servicoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PaginaNegocioService paginaNegocioService;
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private AgendamentoService agendamentoService;

    public String gerarSlug(String nomeNegocio, Long id) {
        String nomeSlug = Normalizer.normalize(nomeNegocio, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();

        return nomeSlug + "-" + id;
    }

    @GetMapping("/prestador/minhapagina")
    public String minhaPagina(Model model) {
        PaginaNegocio pagina = paginaNegocioService.findByUsuario(usuarioService.getUsuarioLogado());
        if (pagina == null) {
            pagina = new PaginaNegocio();
            pagina.setUsuario(usuarioService.getUsuarioLogado());
        }

        model.addAttribute("minhaPagina", pagina);
        model.addAttribute("servicos", servicoService.findAllAtivoByUsuario(usuarioService.getUsuarioLogado()));
        return "/prestador/minha_pagina";
    }

    @PostMapping("/prestador/salvarpagina")
    public ResponseEntity salvarPagina(@RequestBody MinhaPaginaDTO paginaDTO) {
        try {
            PaginaNegocio paginaExistente = paginaNegocioService.findByUsuario(usuarioService.getUsuarioLogado());
            List<Servico> listaServicos = servicoService.findAllById(paginaDTO.servicos());

            // para páginas existentes (edita)
            if (paginaExistente != null) {
                paginaExistente.setNome(paginaDTO.nome());
                paginaExistente.setDescricao(paginaDTO.descricao());
                paginaExistente.setCidade(paginaDTO.cidade());
                paginaExistente.setEndereco(paginaDTO.endereco());
                paginaExistente.setEmail(paginaDTO.email());
                paginaExistente.setTelefone(paginaDTO.telefone());
                paginaExistente.setInstagram(paginaDTO.instagram());

                List<Servico> servicosAtuais = new ArrayList<>(paginaExistente.getServicos());

                // remove serviços que nao estao mais selecionados
                for (Servico servicoAtual : servicosAtuais) {
                    if (!listaServicos.contains(servicoAtual)) {
                        servicoAtual.setPaginaNegocio(null);
                        paginaExistente.getServicos().remove(servicoAtual);
                    }
                }

                // adiciona serviços
                for (Servico servicoSelecionado : listaServicos) {
                    servicoSelecionado.setPaginaNegocio(paginaExistente);
                    if (!paginaExistente.getServicos().contains(servicoSelecionado)) {
                        paginaExistente.getServicos().add(servicoSelecionado);
                    }
                }
                paginaNegocioService.salvar(paginaExistente);
                return ResponseEntity.ok().build();
            }


            // para páginas novas (cria)
            PaginaNegocio paginaNova = new PaginaNegocio();
            paginaNova.setNome(paginaDTO.nome());
            paginaNova.setDescricao(paginaDTO.descricao());
            paginaNova.setCidade(paginaDTO.cidade());
            paginaNova.setEndereco(paginaDTO.endereco());
            for (Servico servico : listaServicos) {
                servico.setPaginaNegocio(paginaNova);
            }
            paginaNova.setServicos(listaServicos);
            paginaNova.setEmail(paginaDTO.email());
            paginaNova.setTelefone(paginaDTO.telefone());
            paginaNova.setInstagram(paginaDTO.instagram());
            paginaNova.setUsuario(usuarioService.getUsuarioLogado());
            paginaNegocioService.salvar(paginaNova);

            // salvando novamente para fazer o slug (url) da pagina
            paginaNova.setSlug(gerarSlug(paginaNova.getNome(), paginaNova.getId()));
            paginaNegocioService.salvar(paginaNova);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/negocio/{slug}")
    public String paginaPublica(@PathVariable String slug, Model model) {
        PaginaNegocio pagina = paginaNegocioService.findBySlug(slug);
        Horario horarioNegocio = horarioService.findAllByUsuario(pagina.getUsuario());
        List<Agendamento> agendamentos = agendamentoService.findAllByUsuario(pagina.getUsuario());
        agendamentos.sort(Comparator.comparing(Agendamento::getData));
        List<String> datasAgendadas = agendamentos.stream().map(a -> a.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).toList();
        Map<String, List<String>> horariosPorData = agendamentos.stream().collect(Collectors.groupingBy(a -> a.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                Collectors.mapping(Agendamento::getHorario, Collectors.toList())));
        List<String> horariosDisponiveis = agendamentoService.gerarHorariosDisponiveis(horarioNegocio);
        List<Servico> servicosAtivos = servicoService.findAllAtivoByUsuario(pagina.getUsuario());

        model.addAttribute("datasAgendadas", datasAgendadas);
        model.addAttribute("pagina", pagina);
        model.addAttribute("horarioNegocio", horarioNegocio);
        model.addAttribute("servicos", pagina.getServicos());
        model.addAttribute("horariosDisponiveis", horariosDisponiveis);
        model.addAttribute("horariosPorData", horariosPorData);
        return "pagina_negocio";
    }

    @GetMapping("/negocio/verificarlogin")
    @ResponseBody
    public ResponseEntity<Void> verificarLogin() {
        if (usuarioService.getUsuarioLogado() != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}

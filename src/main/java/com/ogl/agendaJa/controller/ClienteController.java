package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.model.Cliente;
import com.ogl.agendaJa.model.dto.ClienteDTO;
import com.ogl.agendaJa.services.ClienteService;
import com.ogl.agendaJa.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Comparator;
import java.util.List;

@Controller
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/prestador/clientes")
    public String clientes(Model model) {
        List<Cliente> clientes = clienteService.findAllByPrestador(usuarioService.getUsuarioLogado());
        clientes.sort(Comparator.comparing(Cliente::getNome, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("qtdeClientes", clienteService.qtdeClientePorPrestador(usuarioService.getUsuarioLogado()));
        model.addAttribute("clientes", clientes);
        return "prestador/clientes";
    }

    @PostMapping("/prestador/salvarcliente")
    public ResponseEntity salvarCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(clienteDTO.nome());
            cliente.setEmail(clienteDTO.email());
            cliente.setTelefone(clienteDTO.telefone());
            cliente.setPrestador(usuarioService.getUsuarioLogado());

            clienteService.salvar(cliente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

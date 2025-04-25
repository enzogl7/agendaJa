package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Cliente;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public void salvar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public List<Cliente> findAllByPrestador(Usuario usuario) {
        return clienteRepository.findAllByPrestadorId(usuario.getId());
    }

    public Long qtdeClientePorPrestador(Usuario usuarioLogado) {
        return clienteRepository.countClientesByPrestador_Id(usuarioLogado.getId());
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}

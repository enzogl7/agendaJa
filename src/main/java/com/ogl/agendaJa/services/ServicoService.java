package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Servico;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    public void salvar(Servico servico) {
        servicoRepository.save(servico);
    }

    public void excluir(Servico servico) {
        servicoRepository.delete(servico);
    }

    public List<Servico> findAllByUsuario(Usuario usuarioLogado) {
        return servicoRepository.findAllByUsuario(usuarioLogado);
    }

    public Long qtdeServicosPorUsuario(Usuario usuarioLogado) {
        return servicoRepository.countServicosByUsuario(usuarioLogado);
    }

    public Servico findById(Long id) {
        return servicoRepository.findById(id).orElse(null);
    }

    public List<Servico> findAllById(List<Long> ids) {
        return servicoRepository.findAllById(ids);
    }

}

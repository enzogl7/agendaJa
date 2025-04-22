package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.PaginaNegocio;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.PaginaNegocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaginaNegocioService {
    @Autowired
    private PaginaNegocioRepository paginaNegocioRepository;

    public void salvar(PaginaNegocio paginaNegocio) {
        paginaNegocioRepository.save(paginaNegocio);
    }

    public PaginaNegocio findByUsuario(Usuario usuario) {
        return paginaNegocioRepository.findByUsuario(usuario);
    }

    public PaginaNegocio findBySlug(String slug) {
        return paginaNegocioRepository.findBySlug(slug);
    }
}

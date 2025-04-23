package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Horario;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioService {
    @Autowired
    private HorarioRepository horarioRepository;

    public void salvar(Horario horario) {
        horarioRepository.save(horario);
    }

    public Horario findAllByUsuario(Usuario usuario) {
        return horarioRepository.findAllByUsuario(usuario);
    }
}

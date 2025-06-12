package com.ogl.agendaJa.services;

import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getUsuarioLogado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return (Usuario) usuarioRepository.findByEmail(username);
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).get();
    }

    public void salvar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void ativarPlano(String email, String costumerId) {
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        usuario.setPagamentoConfirmado(true);
        usuario.setCostumerIdStripe(costumerId);
        usuarioRepository.save(usuario);
        System.out.println("Pagamento confirmado TRUE para: " + email);
    }
    public void desativarPlano(String costumerId) {
        Usuario usuario = (Usuario) usuarioRepository.findByCostumerIdStripe(costumerId);
        usuario.setPagamentoConfirmado(false);
        usuarioRepository.save(usuario);
        System.out.println("Pagamento confirmado FALSE para: " + usuario.getEmail());
    }
}


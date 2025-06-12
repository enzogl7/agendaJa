package com.ogl.agendaJa.repository;

import com.ogl.agendaJa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
     UserDetails findByEmail(String email);

     Usuario findByCpf(String cpf);

     Usuario findByCostumerIdStripe(String costumerIdStripe);
}

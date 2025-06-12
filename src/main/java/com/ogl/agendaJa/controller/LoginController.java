package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.infra.security.TokenService;
import com.ogl.agendaJa.model.dto.AuthenticationDTO;
import com.ogl.agendaJa.model.dto.RegisterDTO;
import com.ogl.agendaJa.model.Usuario;
import com.ogl.agendaJa.repository.UsuarioRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO dataRegister) {
        if (this.usuarioRepository.findByEmail(dataRegister.email()) != null) return ResponseEntity.badRequest().build();
        if (!dataRegister.cpf().equals("")) {
            Usuario usuario = usuarioRepository.findByCpf(dataRegister.cpf());
            if (usuario != null) {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(dataRegister.senha());
        Usuario newUsuario = new Usuario(dataRegister.nome(), dataRegister.email(), encryptedPassword,
                dataRegister.cpf(), dataRegister.dataNascimento(),
                dataRegister.planoSelecionado(), !dataRegister.preRegistro(), dataRegister.userRole());

        usuarioRepository.save(newUsuario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logar")
    public ResponseEntity logar(@RequestBody AuthenticationDTO dataLogin, HttpServletResponse response) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dataLogin.email(), dataLogin.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);

            response.addCookie(cookie);
            Usuario usuario = (Usuario) usuarioRepository.findByEmail(dataLogin.email());
            if (usuario.getAuthorities().stream().anyMatch(auth1 -> auth1.getAuthority().equals("ROLE_CLIENTE"))) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            } else {
                return ResponseEntity.ok().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

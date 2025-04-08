package com.ogl.agendaJa.controller;

import com.ogl.agendaJa.infra.security.TokenService;
import com.ogl.agendaJa.model.AuthenticationDTO;
import com.ogl.agendaJa.model.RegisterDTO;
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO dataRegister) {
        if (this.usuarioRepository.findByEmail(dataRegister.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dataRegister.senha());
        Usuario newUsuario = new Usuario(dataRegister.nome(), dataRegister.email(), encryptedPassword,
                dataRegister.cpf(), dataRegister.tipoUsuario(), dataRegister.dataNascimento(),
                dataRegister. planoSelecionado(), true, dataRegister.userRole());

        usuarioRepository.save(newUsuario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logar")
    public ResponseEntity logar(@RequestBody AuthenticationDTO dataLogin, HttpServletResponse response) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dataLogin.email(), dataLogin.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);  // Tempo de expiração: 1 hora

            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

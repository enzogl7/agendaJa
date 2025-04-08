package com.ogl.agendaJa.model;

import java.time.LocalDate;

public record RegisterDTO(String nome, String email, String senha, String cpf, LocalDate dataNascimento, TipoUsuario tipoUsuario, String planoSelecionado, UserRole userRole) {
}

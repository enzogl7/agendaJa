package com.ogl.agendaJa.model.dto;

import com.ogl.agendaJa.model.UserRole;

import java.time.LocalDate;

public record RegisterDTO(String nome, String email, String senha, String cpf,
                          LocalDate dataNascimento, String planoSelecionado, UserRole userRole) {
}

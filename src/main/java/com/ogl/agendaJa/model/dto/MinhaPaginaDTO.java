package com.ogl.agendaJa.model.dto;

import java.util.List;

public record MinhaPaginaDTO(String nome, String descricao, String endereco, String cidade, List<Long> servicos, String telefone, String email, String instagram) {
}

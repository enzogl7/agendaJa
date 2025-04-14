package com.ogl.agendaJa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "servicos")
@Table(name = "servicos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String descricao;
    private String preco;
    private String categoria;
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}

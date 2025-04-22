package com.ogl.agendaJa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "paginaNegocio")
@Table(name = "paginaNegocio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginaNegocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private String cidade;
    private String endereco;
    @OneToMany(mappedBy = "paginaNegocio", cascade = CascadeType.ALL)
    private List<Servico> servicos;
    private String telefone;
    private String email;
    @OneToOne
    private Usuario usuario;
    private String slug;
    private String instagram;
}

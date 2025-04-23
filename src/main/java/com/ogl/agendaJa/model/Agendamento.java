package com.ogl.agendaJa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "agendamentos")
@Table(name = "agendamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Servico servico;
    private LocalDate data;
    private String horario;
    @ManyToOne
    private Usuario prestador;
    private String status;
    @ManyToOne
    private Usuario cliente;
    private String formaPagamento;
}

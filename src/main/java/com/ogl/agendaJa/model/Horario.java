package com.ogl.agendaJa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "horarios")
@Table(name = "horarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario usuario;
    private String inicio_expediente;
    private String fim_expediente;
    private String inicio_pausa;
    private String fim_pausa;
    private List<LocalDate> datasFolgas;
}

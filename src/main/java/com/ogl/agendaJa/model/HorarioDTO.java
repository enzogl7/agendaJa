package com.ogl.agendaJa.model;

import java.time.LocalDate;
import java.util.List;

public record HorarioDTO(String inicio_expediente, String fim_expediente, String inicio_pausa, String fim_pausa, List<LocalDate> datasFolga) {
}

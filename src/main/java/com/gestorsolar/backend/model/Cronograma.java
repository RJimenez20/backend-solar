package com.gestorsolar.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "CRONOGRAMA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cronograma {

    @Id
    @Column(name = "id_proyecto")
    private Long idProyecto;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_aprobacion")
    private LocalDate fechaAprobacion;

    @Column(name = "fecha_ingreso_medidor")
    private LocalDate fechaIngresoMedidor;

    @Column(name = "fecha_instalacion_medidor")
    private LocalDate fechaInstalacionMedidor;

    @Column(name = "fecha_visita_retie")
    private LocalDate fechaVisitaRetie;

    @Column(name = "fecha_visita_agpe")
    private LocalDate fechaVisitaAgpe;
}

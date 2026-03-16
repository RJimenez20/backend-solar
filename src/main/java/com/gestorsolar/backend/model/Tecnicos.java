package com.gestorsolar.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "TECNICOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tecnicos {

    @Id
    @Column(name = "id_proyecto")
    private Long idProyecto;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @Column(name = "potencia_ac", nullable = false)
    private BigDecimal potenciaAc;

    @Column(name = "marca_inversor", nullable = false)
    private String marcaInversor;

    @Column(name = "modelo_inversor", nullable = false)
    private String modeloInversor;

    @Column(name = "cantidad_inversores", nullable = false)
    private Integer cantidadInversores;

    @Column(name = "potencia_dc", nullable = false)
    private BigDecimal potenciaDc;

    @Column(name = "marca_panel", nullable = false)
    private String marcaPanel;

    @Column(name = "modelo_panel", nullable = false)
    private String modeloPanel;

    @Column(name = "cantidad_paneles", nullable = false)
    private Integer cantidadPaneles;
}

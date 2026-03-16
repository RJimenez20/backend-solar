package com.gestorsolar.backend.dto;

import com.gestorsolar.backend.model.TipoProyecto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.math.BigDecimal;

@Data
public class ProyectoDTO {
    private Long id;

    @Valid
    @NotNull
    private ClienteDTO cliente;

    @NotNull
    private TipoProyecto tipoProyecto;

    private Integer estrato;

    private String numeroUsuarioEnergia;
    private String numeroTransformador;

    @Valid
    @NotNull
    private TecnicosDTO tecnicos;

    private CronogramaDTO cronograma;

    @Data
    public static class ClienteDTO {
        @NotNull
        private String cedulaNit;
        @NotNull
        private String nombre;
        private String correo;
        private String direccion;
        private String telefono;
        private String banco;
        private String tipoCuenta;
        private String numeroCuenta;
    }

    @Data
    public static class TecnicosDTO {
        @NotNull
        private BigDecimal potenciaAc;
        @NotNull
        private String marcaInversor;
        @NotNull
        private String modeloInversor;
        @NotNull
        private Integer cantidadInversores;
        @NotNull
        private BigDecimal potenciaDc;
        @NotNull
        private String marcaPanel;
        @NotNull
        private String modeloPanel;
        @NotNull
        private Integer cantidadPaneles;
    }

    @Data
    public static class CronogramaDTO {
        private LocalDate fechaIngreso;
        private LocalDate fechaAprobacion;
        private LocalDate fechaIngresoMedidor;
        private LocalDate fechaInstalacionMedidor;
        private LocalDate fechaVisitaRetie;
        private LocalDate fechaVisitaAgpe;
    }
}

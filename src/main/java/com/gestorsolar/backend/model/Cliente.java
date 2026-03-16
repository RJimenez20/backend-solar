package com.gestorsolar.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CLIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column(name = "cedula_nit", length = 50)
    private String cedulaNit;

    @Column(nullable = false)
    private String nombre;

    private String correo;
    private String direccion;
    private String telefono;
    private String banco;

    @Column(name = "tipo_cuenta")
    private String tipoCuenta;

    @Column(name = "numero_cuenta")
    private String numeroCuenta;
}

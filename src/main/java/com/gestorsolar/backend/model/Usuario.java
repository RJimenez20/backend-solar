package com.gestorsolar.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUSUARIO")
    private Long id;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Column(length = 50)
    private String rol = "ROLE_ENGINEER";

    @Column(name = "verificado", nullable = false)
    private boolean verificado = false;

    @Column(name = "codigo_verificacion", length = 100)
    private String codigoVerificacion;

    @Column(length = 255)
    private String nombre;

    @Column(length = 100)
    private String cargo;

    @Column(length = 50)
    private String telefono;

    @Lob
    @Column(name = "foto_base64", columnDefinition = "LONGTEXT")
    private String fotoBase64;
}


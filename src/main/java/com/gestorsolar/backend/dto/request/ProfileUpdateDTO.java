package com.gestorsolar.backend.dto.request;

import lombok.Data;

@Data
public class ProfileUpdateDTO {
    private String nombre;
    private String cargo;
    private String telefono;
    private String fotoBase64;
}

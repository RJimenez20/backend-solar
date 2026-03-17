package com.gestorsolar.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String correo;
    private String nombre;
    private String cargo;
    private String telefono;
    private String fotoBase64;
}

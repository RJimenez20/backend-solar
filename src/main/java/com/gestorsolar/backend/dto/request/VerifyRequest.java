package com.gestorsolar.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyRequest {
    @NotBlank
    @Email
    private String correo;

    @NotBlank
    private String codigoVerificacion;
}

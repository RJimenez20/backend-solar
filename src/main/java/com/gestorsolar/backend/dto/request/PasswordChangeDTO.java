package com.gestorsolar.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordChangeDTO {
    @NotBlank
    private String oldPassword;
    
    @NotBlank
    private String newPassword;
    
    @NotBlank
    private String confirmPassword;
}

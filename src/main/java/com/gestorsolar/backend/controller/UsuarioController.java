package com.gestorsolar.backend.controller;

import com.gestorsolar.backend.dto.request.PasswordChangeDTO;
import com.gestorsolar.backend.dto.request.ProfileUpdateDTO;
import com.gestorsolar.backend.dto.response.MessageResponse;
import com.gestorsolar.backend.dto.response.ProfileResponse;
import com.gestorsolar.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.getProfile(correo));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody ProfileUpdateDTO dto) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.updateProfile(correo, dto));
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDTO dto) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            usuarioService.changePassword(correo, dto);
            return ResponseEntity.ok(new MessageResponse("Contraseña actualizada con éxito"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}

package com.gestorsolar.backend.service;

import com.gestorsolar.backend.dto.request.PasswordChangeDTO;
import com.gestorsolar.backend.dto.request.ProfileUpdateDTO;
import com.gestorsolar.backend.dto.response.ProfileResponse;
import com.gestorsolar.backend.model.Usuario;
import com.gestorsolar.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponse getProfile(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return new ProfileResponse(
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getCargo(),
                usuario.getTelefono(),
                usuario.getFotoBase64()
        );
    }

    @Transactional
    public ProfileResponse updateProfile(String correo, ProfileUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setCargo(dto.getCargo());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFotoBase64(dto.getFotoBase64());

        usuarioRepository.save(usuario);

        return new ProfileResponse(
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getCargo(),
                usuario.getTelefono(),
                usuario.getFotoBase64()
        );
    }

    @Transactional
    public void changePassword(String correo, PasswordChangeDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getOldPassword(), usuario.getContrasena())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Las nuevas contraseñas no coinciden");
        }

        usuario.setContrasena(passwordEncoder.encode(dto.getNewPassword()));
        usuarioRepository.save(usuario);
    }
}

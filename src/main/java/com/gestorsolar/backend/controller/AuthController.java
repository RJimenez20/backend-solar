package com.gestorsolar.backend.controller;

import com.gestorsolar.backend.dto.request.LoginRequest;
import com.gestorsolar.backend.dto.request.RegisterRequest;
import com.gestorsolar.backend.dto.request.VerifyRequest;
import com.gestorsolar.backend.dto.response.JwtResponse;
import com.gestorsolar.backend.dto.response.MessageResponse;
import com.gestorsolar.backend.model.Usuario;
import com.gestorsolar.backend.repository.UsuarioRepository;
import com.gestorsolar.backend.security.jwt.JwtUtils;
import com.gestorsolar.backend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getContrasena()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // En un caso real, validar si userDetails.isVerified() es true, si no rechazar
        if (!userDetails.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Cuenta no verificada"));
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (usuarioRepository.existsByCorreo(signUpRequest.getCorreo())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El correo electrónico ya está en uso."));
        }

        Usuario user = new Usuario();
        user.setCorreo(signUpRequest.getCorreo());
        user.setContrasena(encoder.encode(signUpRequest.getContrasena()));
        
        // Generar código de verificación de 4 dígitos
        String code = String.format("%04d", new Random().nextInt(10000));
        user.setCodigoVerificacion(code);
        user.setVerificado(false);

        usuarioRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado con éxito. Código de verificación: " + code));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyRequest verifyRequest) {
        Optional<Usuario> userOptional = usuarioRepository.findByCorreo(verifyRequest.getCorreo());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario no encontrado."));
        }

        Usuario user = userOptional.get();
        if (user.isVerificado()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El usuario ya está verificado."));
        }

        if (user.getCodigoVerificacion().equals(verifyRequest.getCodigoVerificacion())) {
            user.setVerificado(true);
            user.setCodigoVerificacion(null);
            usuarioRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Usuario verificado con éxito."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Código de verificación incorrecto."));
        }
    }
}

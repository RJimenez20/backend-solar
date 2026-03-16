package com.gestorsolar.backend.controller;

import com.gestorsolar.backend.dto.ProyectoDTO;
import com.gestorsolar.backend.dto.response.DashboardStatsResponse;
import com.gestorsolar.backend.dto.response.MessageResponse;
import com.gestorsolar.backend.service.ProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(proyectoService.getDashboardStats());
    }

    @GetMapping("/proyectos")
    public ResponseEntity<List<ProyectoDTO>> getAllProyectos() {
        return ResponseEntity.ok(proyectoService.getAllProyectos());
    }

    @GetMapping("/proyectos/{id}")
    public ResponseEntity<ProyectoDTO> getProyectoById(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.getProyectoById(id));
    }

    @PostMapping("/proyectos")
    public ResponseEntity<ProyectoDTO> createProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) {
        return ResponseEntity.ok(proyectoService.createProyecto(proyectoDTO));
    }

    @PutMapping("/proyectos/{id}")
    public ResponseEntity<ProyectoDTO> updateProyecto(@PathVariable Long id, @Valid @RequestBody ProyectoDTO proyectoDTO) {
        return ResponseEntity.ok(proyectoService.updateProyecto(id, proyectoDTO));
    }

    @DeleteMapping("/proyectos/{id}")
    public ResponseEntity<MessageResponse> deleteProyecto(@PathVariable Long id) {
        proyectoService.deleteProyecto(id);
        return ResponseEntity.ok(new MessageResponse("Proyecto eliminado correctamente (Soft Delete)"));
    }
}

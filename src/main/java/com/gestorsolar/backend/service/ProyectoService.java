package com.gestorsolar.backend.service;

import com.gestorsolar.backend.dto.ProyectoDTO;
import com.gestorsolar.backend.dto.response.DashboardStatsResponse;
import com.gestorsolar.backend.exception.ResourceNotFoundException;
import com.gestorsolar.backend.model.*;
import com.gestorsolar.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ProyectoDTO createProyecto(ProyectoDTO dto) {
        Long usuarioId = getCurrentUsuarioId();
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Guardar o actualizar cliente
        Cliente cliente = clienteRepository.findById(dto.getCliente().getCedulaNit())
                .orElseGet(() -> {
                    Cliente nuevoCliente = new Cliente();
                    nuevoCliente.setCedulaNit(dto.getCliente().getCedulaNit());
                    nuevoCliente.setNombre(dto.getCliente().getNombre());
                    nuevoCliente.setCorreo(dto.getCliente().getCorreo());
                    nuevoCliente.setDireccion(dto.getCliente().getDireccion());
                    nuevoCliente.setTelefono(dto.getCliente().getTelefono());
                    nuevoCliente.setBanco(dto.getCliente().getBanco());
                    nuevoCliente.setTipoCuenta(dto.getCliente().getTipoCuenta());
                    nuevoCliente.setNumeroCuenta(dto.getCliente().getNumeroCuenta());
                    return clienteRepository.save(nuevoCliente);
                });

        Proyecto proyecto = new Proyecto();
        proyecto.setCliente(cliente);
        proyecto.setUsuario(usuario); // Asignar el dueño
        proyecto.setTipoProyecto(dto.getTipoProyecto());
        proyecto.setEstrato(dto.getEstrato());
        proyecto.setNumeroUsuarioEnergia(dto.getNumeroUsuarioEnergia());
        proyecto.setNumeroTransformador(dto.getNumeroTransformador());
        proyecto.setDeleted(false);

        // Tecnicos
        Tecnicos tecnicos = new Tecnicos();
        tecnicos.setProyecto(proyecto);
        tecnicos.setPotenciaAc(dto.getTecnicos().getPotenciaAc());
        tecnicos.setMarcaInversor(dto.getTecnicos().getMarcaInversor());
        tecnicos.setModeloInversor(dto.getTecnicos().getModeloInversor());
        tecnicos.setCantidadInversores(dto.getTecnicos().getCantidadInversores());
        tecnicos.setPotenciaDc(dto.getTecnicos().getPotenciaDc());
        tecnicos.setMarcaPanel(dto.getTecnicos().getMarcaPanel());
        tecnicos.setModeloPanel(dto.getTecnicos().getModeloPanel());
        tecnicos.setCantidadPaneles(dto.getTecnicos().getCantidadPaneles());
        proyecto.setTecnicos(tecnicos);

        // Cronograma
        if(dto.getCronograma() != null) {
            Cronograma cronograma = new Cronograma();
            cronograma.setProyecto(proyecto);
            cronograma.setFechaIngreso(dto.getCronograma().getFechaIngreso());
            cronograma.setFechaAprobacion(dto.getCronograma().getFechaAprobacion());
            cronograma.setFechaIngresoMedidor(dto.getCronograma().getFechaIngresoMedidor());
            cronograma.setFechaInstalacionMedidor(dto.getCronograma().getFechaInstalacionMedidor());
            cronograma.setFechaVisitaRetie(dto.getCronograma().getFechaVisitaRetie());
            cronograma.setFechaVisitaAgpe(dto.getCronograma().getFechaVisitaAgpe());
            proyecto.setCronograma(cronograma);
        }

        Proyecto saved = proyectoRepository.save(proyecto);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ProyectoDTO> getAllProyectos() {
        Long usuarioId = getCurrentUsuarioId();
        return proyectoRepository.findAllByDeletedFalseAndUsuarioIdOrderByIdDesc(usuarioId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProyectoDTO getProyectoById(Long id) {
        Long usuarioId = getCurrentUsuarioId();
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id " + id));
        
        if (!proyecto.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tiene permisos para ver este proyecto");
        }
        
        return mapToDTO(proyecto);
    }

    @Transactional
    public ProyectoDTO updateProyecto(Long id, ProyectoDTO dto) {
        Long usuarioId = getCurrentUsuarioId();
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id " + id));

        if (!proyecto.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tiene permisos para editar este proyecto");
        }

        // Actualizar datos del cliente asumiendo que es el mismo o actualizar entidad entera
        Cliente cliente = proyecto.getCliente();
        cliente.setNombre(dto.getCliente().getNombre());
        cliente.setCorreo(dto.getCliente().getCorreo());
        cliente.setDireccion(dto.getCliente().getDireccion());
        cliente.setTelefono(dto.getCliente().getTelefono());
        cliente.setBanco(dto.getCliente().getBanco());
        cliente.setTipoCuenta(dto.getCliente().getTipoCuenta());
        cliente.setNumeroCuenta(dto.getCliente().getNumeroCuenta());
        clienteRepository.save(cliente);

        proyecto.setTipoProyecto(dto.getTipoProyecto());
        if(dto.getTipoProyecto() == TipoProyecto.RESIDENCIAL) {
            proyecto.setEstrato(dto.getEstrato());
        } else {
            proyecto.setEstrato(null);
        }
        proyecto.setNumeroUsuarioEnergia(dto.getNumeroUsuarioEnergia());
        proyecto.setNumeroTransformador(dto.getNumeroTransformador());

        Tecnicos tecnicos = proyecto.getTecnicos();
        tecnicos.setPotenciaAc(dto.getTecnicos().getPotenciaAc());
        tecnicos.setMarcaInversor(dto.getTecnicos().getMarcaInversor());
        tecnicos.setModeloInversor(dto.getTecnicos().getModeloInversor());
        tecnicos.setCantidadInversores(dto.getTecnicos().getCantidadInversores());
        tecnicos.setPotenciaDc(dto.getTecnicos().getPotenciaDc());
        tecnicos.setMarcaPanel(dto.getTecnicos().getMarcaPanel());
        tecnicos.setModeloPanel(dto.getTecnicos().getModeloPanel());
        tecnicos.setCantidadPaneles(dto.getTecnicos().getCantidadPaneles());

        if (dto.getCronograma() != null) {
            Cronograma cronograma = proyecto.getCronograma();
            if (cronograma == null) {
                cronograma = new Cronograma();
                cronograma.setProyecto(proyecto);
                proyecto.setCronograma(cronograma);
            }
            cronograma.setFechaIngreso(dto.getCronograma().getFechaIngreso());
            cronograma.setFechaAprobacion(dto.getCronograma().getFechaAprobacion());
            cronograma.setFechaIngresoMedidor(dto.getCronograma().getFechaIngresoMedidor());
            cronograma.setFechaInstalacionMedidor(dto.getCronograma().getFechaInstalacionMedidor());
            cronograma.setFechaVisitaRetie(dto.getCronograma().getFechaVisitaRetie());
            cronograma.setFechaVisitaAgpe(dto.getCronograma().getFechaVisitaAgpe());
        }

        return mapToDTO(proyectoRepository.save(proyecto));
    }

    @Transactional
    public void deleteProyecto(Long id) {
        Long usuarioId = getCurrentUsuarioId();
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id " + id));
        
        if (!proyecto.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tiene permisos para eliminar este proyecto");
        }
        
        proyecto.setDeleted(true);
        proyectoRepository.save(proyecto);
        proyectoRepository.flush(); // Asegurar visibilidad inmediata del cambio
        log.info("Project {} marked as deleted and flushed", id);
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        Long usuarioId = getCurrentUsuarioId();
        long totalProyectos = proyectoRepository.countByDeletedFalseAndUsuarioId(usuarioId);
        BigDecimal pAc = proyectoRepository.sumPotenciaAc(usuarioId);
        BigDecimal pDc = proyectoRepository.sumPotenciaDc(usuarioId);
        Long paneles = proyectoRepository.sumCantidadPaneles(usuarioId);

        log.info("Stats calculation for user {}: total={}, pAc={}, pDc={}, panels={}", 
            usuarioId, totalProyectos, pAc, pDc, paneles);

        return DashboardStatsResponse.builder()
                .totalProyectos(totalProyectos)
                .potenciaAcTotal(pAc != null ? pAc : BigDecimal.ZERO)
                .potenciaDcTotal(pDc != null ? pDc : BigDecimal.ZERO)
                .panelesTotales(paneles != null ? paneles : 0)
                .residencialCount(proyectoRepository.countByTipoProyectoAndDeletedFalseAndUsuarioId(TipoProyecto.RESIDENCIAL, usuarioId))
                .comercialCount(proyectoRepository.countByTipoProyectoAndDeletedFalseAndUsuarioId(TipoProyecto.COMERCIAL, usuarioId))
                .industrialCount(proyectoRepository.countByTipoProyectoAndDeletedFalseAndUsuarioId(TipoProyecto.INDUSTRIAL, usuarioId))
                .enProgresoCount(proyectoRepository.countEnProgreso(usuarioId))
                .completadosCount(proyectoRepository.countCompletados(usuarioId))
                .build();
    }

    private Long getCurrentUsuarioId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        String email = auth.getName();
        return usuarioRepository.findByCorreo(email)
                .map(Usuario::getId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + email));
    }

    private ProyectoDTO mapToDTO(Proyecto proyecto) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(proyecto.getId());
        dto.setTipoProyecto(proyecto.getTipoProyecto());
        dto.setEstrato(proyecto.getEstrato());
        dto.setNumeroUsuarioEnergia(proyecto.getNumeroUsuarioEnergia());
        dto.setNumeroTransformador(proyecto.getNumeroTransformador());

        ProyectoDTO.ClienteDTO cDto = new ProyectoDTO.ClienteDTO();
        cDto.setCedulaNit(proyecto.getCliente().getCedulaNit());
        cDto.setNombre(proyecto.getCliente().getNombre());
        cDto.setCorreo(proyecto.getCliente().getCorreo());
        cDto.setDireccion(proyecto.getCliente().getDireccion());
        cDto.setTelefono(proyecto.getCliente().getTelefono());
        cDto.setBanco(proyecto.getCliente().getBanco());
        cDto.setTipoCuenta(proyecto.getCliente().getTipoCuenta());
        cDto.setNumeroCuenta(proyecto.getCliente().getNumeroCuenta());
        dto.setCliente(cDto);

        if (proyecto.getTecnicos() != null) {
            ProyectoDTO.TecnicosDTO tDto = new ProyectoDTO.TecnicosDTO();
            tDto.setPotenciaAc(proyecto.getTecnicos().getPotenciaAc());
            tDto.setMarcaInversor(proyecto.getTecnicos().getMarcaInversor());
            tDto.setModeloInversor(proyecto.getTecnicos().getModeloInversor());
            tDto.setCantidadInversores(proyecto.getTecnicos().getCantidadInversores());
            tDto.setPotenciaDc(proyecto.getTecnicos().getPotenciaDc());
            tDto.setMarcaPanel(proyecto.getTecnicos().getMarcaPanel());
            tDto.setModeloPanel(proyecto.getTecnicos().getModeloPanel());
            tDto.setCantidadPaneles(proyecto.getTecnicos().getCantidadPaneles());
            dto.setTecnicos(tDto);
        }

        if (proyecto.getCronograma() != null) {
            ProyectoDTO.CronogramaDTO croDto = new ProyectoDTO.CronogramaDTO();
            croDto.setFechaIngreso(proyecto.getCronograma().getFechaIngreso());
            croDto.setFechaAprobacion(proyecto.getCronograma().getFechaAprobacion());
            croDto.setFechaIngresoMedidor(proyecto.getCronograma().getFechaIngresoMedidor());
            croDto.setFechaInstalacionMedidor(proyecto.getCronograma().getFechaInstalacionMedidor());
            croDto.setFechaVisitaRetie(proyecto.getCronograma().getFechaVisitaRetie());
            croDto.setFechaVisitaAgpe(proyecto.getCronograma().getFechaVisitaAgpe());
            dto.setCronograma(croDto);
        }

        return dto;
    }
}

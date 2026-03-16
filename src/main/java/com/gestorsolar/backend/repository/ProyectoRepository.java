package com.gestorsolar.backend.repository;

import com.gestorsolar.backend.model.Proyecto;
import com.gestorsolar.backend.model.TipoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    @Query("SELECT COALESCE(SUM(p.tecnicos.potenciaAc), 0) FROM Proyecto p WHERE p.deleted = false AND p.usuario.id = :usuarioId")
    BigDecimal sumPotenciaAc(Long usuarioId);

    @Query("SELECT COALESCE(SUM(p.tecnicos.potenciaDc), 0) FROM Proyecto p WHERE p.deleted = false AND p.usuario.id = :usuarioId")
    BigDecimal sumPotenciaDc(Long usuarioId);

    @Query("SELECT COALESCE(SUM(p.tecnicos.cantidadPaneles), 0) FROM Proyecto p WHERE p.deleted = false AND p.usuario.id = :usuarioId")
    Long sumCantidadPaneles(Long usuarioId);

    long countByDeletedFalseAndUsuarioId(Long usuarioId);

    List<Proyecto> findAllByDeletedFalseAndUsuarioIdOrderByIdDesc(Long usuarioId);

    long countByTipoProyectoAndDeletedFalseAndUsuarioId(TipoProyecto tipo, Long usuarioId);

    @Query("SELECT COUNT(p) FROM Proyecto p WHERE p.deleted = false AND p.usuario.id = :usuarioId AND " +
           "p.cronograma.fechaIngreso IS NOT NULL AND " +
           "p.cronograma.fechaAprobacion IS NOT NULL AND " +
           "p.cronograma.fechaIngresoMedidor IS NOT NULL AND " +
           "p.cronograma.fechaInstalacionMedidor IS NOT NULL AND " +
           "p.cronograma.fechaVisitaRetie IS NOT NULL AND " +
           "p.cronograma.fechaVisitaAgpe IS NOT NULL")
    long countCompletados(Long usuarioId);

    @Query("SELECT COUNT(p) FROM Proyecto p WHERE p.deleted = false AND p.usuario.id = :usuarioId AND (" +
           "p.cronograma IS NULL OR " +
           "p.cronograma.fechaIngreso IS NULL OR " +
           "p.cronograma.fechaAprobacion IS NULL OR " +
           "p.cronograma.fechaIngresoMedidor IS NULL OR " +
           "p.cronograma.fechaInstalacionMedidor IS NULL OR " +
           "p.cronograma.fechaVisitaRetie IS NULL OR " +
           "p.cronograma.fechaVisitaAgpe IS NULL)")
    long countEnProgreso(Long usuarioId);
}

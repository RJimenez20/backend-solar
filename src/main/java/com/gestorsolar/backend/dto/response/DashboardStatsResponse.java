package com.gestorsolar.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalProyectos;
    private BigDecimal potenciaAcTotal;
    private BigDecimal potenciaDcTotal;
    private long panelesTotales;

    // Estadísticas para gráficos
    private long residencialCount;
    private long comercialCount;
    private long industrialCount;
    private long enProgresoCount;
    private long completadosCount;
}

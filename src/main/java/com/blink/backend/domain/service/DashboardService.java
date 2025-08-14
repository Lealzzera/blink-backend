package com.blink.backend.domain.service;


import com.blink.backend.controller.dashboard.DashboardDTO;
import com.blink.backend.persistence.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardDTO getDashboardData(Integer clinicId, LocalDateTime startDate, LocalDateTime endDate) {

        Long totalNovasMensagens = dashboardRepository.countNovasMensagens(clinicId);
        Long totalAgendamentosRealizados = dashboardRepository.countAgendamentosRealizados(clinicId, startDate, endDate);
        Long totalComparecimentos = dashboardRepository.countComparecimentos(clinicId, startDate, endDate);
        Long totalVendas = dashboardRepository.countVendas(clinicId, startDate, endDate);
        BigDecimal totalValorVendas = dashboardRepository.sumValorVendas(clinicId, startDate, endDate);

        /*DashboardService dashboardService = DashboardService.builder() */

        return new DashboardDTO(
                totalNovasMensagens,
                totalAgendamentosRealizados,
                totalComparecimentos,
                totalVendas,
                totalValorVendas
        );

        
    }


}

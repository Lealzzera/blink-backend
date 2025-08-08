package com.blink.backend.domain.service;


import com.blink.backend.controller.dashboard.DashboardDTO;
import com.blink.backend.persistence.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardDTO getDashboardData(Integer clinicId) {

        Long totalNovasMensagens = dashboardRepository.countNovasMensagens(clinicId);
        Long totalAgendamentosRealizados = dashboardRepository.countAgendamentosRealizados(clinicId);
        Long totalComparecimentos = dashboardRepository.countComparecimentos(clinicId);
        Long totalVendas = dashboardRepository.countVendas(clinicId);
        BigDecimal totalValorVendas = dashboardRepository.sumValorVendas(clinicId);

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

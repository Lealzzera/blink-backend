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

        Long receivedMessagesCountTotal = dashboardRepository.countNovasMensagens(clinicId);
        Long appointmentsCountTotal = dashboardRepository.countAgendamentosRealizados(clinicId);
        Long showUpsCountTotal = dashboardRepository.countComparecimentos(clinicId);
        Long salesCountTotal = dashboardRepository.countVendas(clinicId);
        BigDecimal salesValueTotal = dashboardRepository.sumValorVendas(clinicId);

        return DashboardDTO.builder()
                .receivedMessagesCountTotal(receivedMessagesCountTotal)
                .appointmentsCountTotal(appointmentsCountTotal)
                .showUpsCountTotal(showUpsCountTotal)
                .salesCountTotal(salesCountTotal)
                .salesValueTotal(salesValueTotal)
                .build();
    }


}

package com.blink.backend.domain.service;


import com.blink.backend.controller.dashboard.DashboardDTO;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.blink.backend.persistence.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardDTO getDashboardData(Integer clinicId, LocalDateTime startDate, LocalDateTime endDate) {

        Long receivedMessagesCountTotal = dashboardRepository.countNovasMensagens(clinicId, startDate, endDate);
        Long appointmentsCountTotal = dashboardRepository.countAppointmentsForDashboard(clinicId, startDate, endDate,
                List.of("AGENDADO", "CONFIRMADO"));
        Long showUpsCountTotal = dashboardRepository.countAppointmentsForDashboard(clinicId, startDate, endDate,
                List.of("COMPARECEU"));
        Long salesCountTotal = dashboardRepository.countVendas(clinicId, startDate, endDate);
        BigDecimal salesValueTotal = dashboardRepository.sumValorVendas(clinicId, startDate, endDate);

        return DashboardDTO.builder()
                .receivedMessagesCountTotal(receivedMessagesCountTotal)
                .appointmentsCountTotal(appointmentsCountTotal)
                .showUpsCountTotal(showUpsCountTotal)
                .salesCountTotal(salesCountTotal)
                .salesValueTotal(salesValueTotal)
                .build();
    }


}

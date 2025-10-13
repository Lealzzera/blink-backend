package com.blink.backend.domain.service;


import com.blink.backend.controller.dashboard.DashboardDTO;
import com.blink.backend.persistence.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static java.math.BigDecimal.valueOf;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private static final Integer DASHBOARD_PRECISION = 4;
    private final DashboardRepository dashboardRepository;

    public DashboardDTO getDashboardData(Integer clinicId, LocalDateTime startDate, LocalDateTime endDate) {

        Long receivedMessagesCountTotal = dashboardRepository.countNovasMensagens(clinicId, startDate, endDate);
        Long appointmentsCountTotal = dashboardRepository.countAppointmentsForDashboard(clinicId, startDate, endDate,
                List.of("AGENDADO", "CONFIRMADO"));
        Long showUpsCountTotal = dashboardRepository.countAppointmentsForDashboard(clinicId, startDate, endDate,
                List.of("COMPARECEU"));
        Long salesCountTotal = dashboardRepository.countVendas(clinicId, startDate, endDate);
        BigDecimal salesValueTotal = dashboardRepository.sumValorVendas(clinicId, startDate, endDate);

        BigDecimal appointmentRate = receivedMessagesCountTotal > 0 ?
                valueOf(appointmentsCountTotal).divide(
                        valueOf(receivedMessagesCountTotal), DASHBOARD_PRECISION, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal showUpRate = appointmentsCountTotal > 0 ?
                valueOf(showUpsCountTotal).divide(
                        valueOf(appointmentsCountTotal), DASHBOARD_PRECISION, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal salesConversionRate = showUpsCountTotal > 0 ?
                valueOf(salesCountTotal).divide(
                        valueOf(showUpsCountTotal), DASHBOARD_PRECISION, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal roi = showUpsCountTotal > 0 ?
                salesValueTotal.divide(
                        valueOf(showUpsCountTotal), DASHBOARD_PRECISION, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return DashboardDTO.builder()
                .receivedMessagesCountTotal(receivedMessagesCountTotal)
                .appointmentsCountTotal(appointmentsCountTotal)
                .showUpsCountTotal(showUpsCountTotal)
                .salesCountTotal(salesCountTotal)
                .salesValueTotal(salesValueTotal)
                .appointmentRate(appointmentRate)
                .showUpRate(showUpRate)
                .salesConversionRate(salesConversionRate)
                .roi(roi)
                .build();
    }


}

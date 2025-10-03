package com.blink.backend.controller.dashboard;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class DashboardDTO {
    private Long receivedMessagesCountTotal;
    private Long appointmentsCountTotal;
    private Long showUpsCountTotal;
    private Long salesCountTotal;
    private BigDecimal salesValueTotal;
    private BigDecimal appointmentRate;
    private BigDecimal showUpRate;
    private BigDecimal salesConversionRate;
    private BigDecimal roi;
}
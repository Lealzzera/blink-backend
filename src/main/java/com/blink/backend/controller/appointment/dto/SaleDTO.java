package com.blink.backend.controller.appointment.dto;


import com.blink.backend.persistence.entity.appointment.Sale;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SaleDTO {

    private Integer appointmentId;
    private BigDecimal value;
    private Integer serviceType;
    private Integer registeredByUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registeredAt;

    public static SaleDTO fromEntity(Sale sale) {

        return SaleDTO.builder()
                .appointmentId(sale.getAppointment().getId())
                .serviceType(sale.getServiceType().getId())
                .value(sale.getValue())
                .registeredByUser(sale.getRegisteredByUser().getId())
                .registeredAt(sale.getRegisteredAt())
                .build();

    }

}



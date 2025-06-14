package com.blink.backend.controller.appointment.dto;


import com.blink.backend.persistence.entity.appointment.Sale;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SaleDTO {

    private Integer appointmentId;
    private BigDecimal saleValue;
    private Integer serviceType;
    private Integer registeredByUser;

    public static SaleDTO fromEntity(Sale sale) {

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setAppointmentId(sale.getAppointment().getId());
        saleDTO.setServiceType(sale.getAppointment().getId());
        saleDTO.setSaleValue(sale.getSaleValue());
        saleDTO.setRegisteredByUser(sale.getRegisteredByUser().getId());

        return saleDTO;

    }

}



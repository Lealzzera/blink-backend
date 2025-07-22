package com.blink.backend.controller.appointment.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdateSaleStatusDTO {

    private Integer saleId;
    private String newStatus;

}

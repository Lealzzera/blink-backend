package com.blink.backend.controller.sale;

import com.blink.backend.controller.appointment.dto.SaleDTO;
import com.blink.backend.controller.appointment.dto.UpdateSaleStatusDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("sales")
public class SalesController {

    private final SalesService salesService;


    @GetMapping("{id}/details")
    public ResponseEntity<SaleDTO> getSaleDetailsById(
            @PathVariable Integer id)
            throws NotFoundException {
        return ResponseEntity.ok(salesService.getSaleDetailsById(id));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO saleDTO)
            throws NotFoundException {

        SaleDTO createdSale = salesService.createSale(saleDTO);
        return ResponseEntity
                .created(URI.create(createdSale.getAppointmentId().toString()))
                .body(createdSale);

    }

    @PutMapping("status")
    public ResponseEntity<Void> updateSaleStatus(@RequestBody UpdateSaleStatusDTO status)
            throws NotFoundException {
        salesService.updateSaleStatus(status);

        return ResponseEntity.noContent().build();

    }

}

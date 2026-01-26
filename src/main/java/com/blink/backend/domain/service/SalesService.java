package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.SaleDTO;
import com.blink.backend.controller.appointment.dto.UpdateSaleStatusDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.appointment.AppointmentEntity;
import com.blink.backend.persistence.entity.appointment.Sale;
import com.blink.backend.persistence.entity.appointment.SaleStatus;
import com.blink.backend.persistence.entity.auth.UserEntity;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.SaleRepository;
import com.blink.backend.persistence.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SaleRepository saleRepository;
    private final UserEntityRepository userEntityRepository;
    private final AppointmentsRepository appointmentsRepository;

    public SaleDTO getSaleDetailsById(Integer id) throws NotFoundException {
        return saleRepository.findById(id)
                .map(SaleDTO::fromEntity)
                .orElseThrow(() -> new NotFoundException("Venda " + id));
    }

    public SaleDTO createSale(SaleDTO saleDTO) throws NotFoundException {

        AppointmentEntity appointment = appointmentsRepository.findById(saleDTO.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Agendamento " + saleDTO.getAppointmentId()));

        UserEntity user = userEntityRepository.findById(saleDTO.getRegisteredByUser())
                .orElseThrow(() -> new NotFoundException("Usuário"));

        Sale sale = Sale.builder()
                .appointment(appointment)
                .patient(appointment.getPatient())
                .value(saleDTO.getValue())
                .status(SaleStatus.NAO_PAGO)
                .registeredByUser(user)
                .registeredAt(LocalDateTime.now())
                .build();

        sale = saleRepository.save(sale);

        return SaleDTO.fromEntity(sale);
    }

    public void updateSaleStatus(UpdateSaleStatusDTO saleStatus) throws NotFoundException {
        Sale sale = saleRepository.findById(saleStatus.getSaleId())
                .orElseThrow(() -> new NotFoundException("Venda " + saleStatus.getSaleId()));

        sale.setStatus(SaleStatus.valueOf(saleStatus.getNewStatus().toUpperCase()));
        saleRepository.save(sale);

    }

}

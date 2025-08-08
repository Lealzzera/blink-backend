package com.blink.backend.domain.service;

import com.blink.backend.controller.appointment.dto.SaleDTO;
import com.blink.backend.controller.appointment.dto.UpdateSaleStatusDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.entity.appointment.Sale;
import com.blink.backend.persistence.entity.appointment.SaleStatus;
import com.blink.backend.persistence.entity.appointment.ServiceType;
import com.blink.backend.persistence.entity.auth.Users;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import com.blink.backend.persistence.repository.SaleRepository;
import com.blink.backend.persistence.repository.ServiceTypeRepository;
import com.blink.backend.persistence.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SaleRepository saleRepository;
    private final UsersRepository usersRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public SaleDTO getSaleDetailsById(Integer id) throws NotFoundException {
        return saleRepository.findById(id)
                .map(SaleDTO::fromEntity)
                .orElseThrow(() -> new NotFoundException("Venda " + id));
    }

    public SaleDTO createSale(SaleDTO saleDTO) throws NotFoundException {

        Appointment appointment = appointmentsRepository.findById(saleDTO.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Agendamento " + saleDTO.getAppointmentId()));

        ServiceType serviceType = serviceTypeRepository.findById(saleDTO.getServiceType())
                .orElseThrow(()-> new NotFoundException("Tipo de serviço"));

        Users user = usersRepository.findById(saleDTO.getRegisteredByUser())
                .orElseThrow(() -> new NotFoundException("Usuário"));

        Sale sale = Sale.builder()
                .appointment(appointment)
                .patient(appointment.getPatient())
                .serviceType(serviceType)
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

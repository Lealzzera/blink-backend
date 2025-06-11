package com.blink.backend;

import com.blink.backend.controller.appointment.dto.UpdateAppointmentStatusDTO;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.ClinicAvailabilityService;
import com.blink.backend.persistence.entity.appointment.Appointment;
import com.blink.backend.persistence.repository.AppointmentsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClinicAvailabilityServiceTest {
    @Mock
    private AppointmentsRepository appointmentsRepository;
    @InjectMocks
    private ClinicAvailabilityService clinicAvailabilityService;

    @Test
    @DisplayName("Quando nao retornar nenhum agendamento deve lancar excecao")
    void updateAppointmentStatusNotFound() {
        UpdateAppointmentStatusDTO dto = MockObjects.getUpdateAppointmentStatus(1, "ok");

        when(appointmentsRepository.findById(eq(dto.getAppointmentId())))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> clinicAvailabilityService.updateAppointmentStatus(dto));
    }

    @Test
    @DisplayName("Sucesso de atualizar o status do agendamento")
    void updateAppointmentStatusSuccess() {
        UpdateAppointmentStatusDTO dto = MockObjects.getUpdateAppointmentStatus(1, "AGENDADO");
        Appointment appointment = MockObjects.getAppointment();
        when(appointmentsRepository.findById(eq(dto.getAppointmentId())))
                .thenReturn(Optional.of(appointment));

        clinicAvailabilityService.updateAppointmentStatus(dto);

        assertEquals(dto.getNewStatus(), appointment.getAppointmentStatus().name());
        verify(appointmentsRepository).save(eq(appointment));
    }
}

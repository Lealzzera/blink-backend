package com.blink.backend.controller.appointment;

import com.blink.backend.controller.appointment.dto.AppointmentDetailsDTO;
import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.controller.appointment.dto.UpdateAppointmentStatusDTO;
import com.blink.backend.domain.service.ClinicAvailabilityService;
import com.blink.backend.persistence.entity.appointment.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("appointments")
public class AppointmentController {

    private final ClinicAvailabilityService clinicAvailabilityService;

    @GetMapping("availability")
    public ResponseEntity<List<ClinicAvailabilityDTO>> getClinicAvailability(
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam(value = "end_date", required = false) LocalDate endDate,
            @RequestParam(value = "hide_cancelled", required = false, defaultValue = "true") Boolean hideCancelled) {
        endDate = isNull(endDate) ? startDate.plusDays(7) : endDate;

        return ResponseEntity.ok(clinicAvailabilityService.getClinicAvailability(startDate, endDate, hideCancelled));
    }

    @PostMapping
    public ResponseEntity<Void> createAppointment(
            @RequestBody CreateAppointmentDTO createAppointmentDTO) {
        Appointment appointment = clinicAvailabilityService.saveAppointment(createAppointmentDTO);

        return ResponseEntity.created(URI.create(appointment.getId().toString())).build();
    }

    @GetMapping("{id}/details")
    public ResponseEntity<AppointmentDetailsDTO> getAppointmentDetailsById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(clinicAvailabilityService.getAppointmentDetailsById(id));
    }

    @PutMapping("status")
    public ResponseEntity<Void> updateAppointmentStatus(@RequestBody UpdateAppointmentStatusDTO updateStatus) {
        clinicAvailabilityService.updateAppointmentStatus(updateStatus);

        return ResponseEntity.noContent().build();
    }


}

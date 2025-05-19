package com.blink.backend.controller.appointment;

import com.blink.backend.controller.appointment.dto.AppointmentDTO;
import com.blink.backend.controller.appointment.dto.ClinicAvailabilityDTO;
import com.blink.backend.controller.appointment.dto.CreateAppointmentDTO;
import com.blink.backend.domain.service.ClinicAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "end_date", required = false) LocalDate endDate) {
        endDate = isNull(endDate) ? startDate.plusDays(7) : endDate;
        return ResponseEntity.ok(clinicAvailabilityService.getClinicAvailability(startDate, endDate));
    }

    @PostMapping
    public ResponseEntity createAppointment(
            @RequestBody CreateAppointmentDTO createAppointmentDTO){

        clinicAvailabilityService.saveAppointment(createAppointmentDTO);
         return ResponseEntity.noContent().build();

    }

    @GetMapping("{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Integer id){

        return ResponseEntity.ok(clinicAvailabilityService.getAppointmentById(id));

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> cancelAppointment (@PathVariable Integer id){

        clinicAvailabilityService.cancelAppointment(id);

        return ResponseEntity.noContent().build();

    }

}

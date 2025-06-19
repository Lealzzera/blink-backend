package com.blink.backend.controller.domain;

import com.blink.backend.domain.model.message.WhatsAppStatus;
import com.blink.backend.persistence.entity.appointment.AppointmentStatus;
import com.blink.backend.persistence.entity.appointment.WeekDay;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("domain")
public class DomainController {

    @GetMapping("week-days")
    public ResponseEntity<List<WeekDay>> getWeekDays() {
        return ResponseEntity.ok(Arrays.stream(WeekDay.values()).toList());
    }

    @GetMapping("appointment-status")
    public ResponseEntity<List<AppointmentStatus>> getAppointmentStatus() {
        return ResponseEntity.ok(Arrays.stream(AppointmentStatus.values()).toList());
    }

    @GetMapping("whats-app-status")
    public ResponseEntity<List<WhatsAppStatus>> getWhatsAppStatus() {
        return ResponseEntity.ok(Arrays.stream(WhatsAppStatus.values()).toList());
    }
}

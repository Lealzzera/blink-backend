package com.blink.backend.controller.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("appointments")
public class AppointmentController {

    @GetMapping("availability")
    public ResponseEntity<Void> getClinicAvailability(){
        return ResponseEntity.ok().build();
    }

}

package com.blink.backend.controller.configuration;


import com.blink.backend.controller.configuration.dto.AvailabilityConfigurationDTO;
import com.blink.backend.domain.service.ConfigurationAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("configurations")
public class ClinicConfigurationController {

    private ConfigurationAvailabilityService configurationAvailabilityService;

    @PutMapping("availability")
    public ResponseEntity<Void> updateAvailabilityConfiguration(@RequestBody List<AvailabilityConfigurationDTO> updateAvailabilityConfiguration){

        configurationAvailabilityService.updateAvailabilityConfiguration(updateAvailabilityConfiguration);

        return ResponseEntity.noContent().build();

    }

}

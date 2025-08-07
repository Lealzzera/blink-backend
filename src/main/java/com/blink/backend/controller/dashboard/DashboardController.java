package com.blink.backend.controller.dashboard;

import com.blink.backend.domain.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("{clinicId}")
    public DashboardDTO getDashboard(@PathVariable Integer clinicId) {

        return dashboardService.getDashboardData(clinicId);
    }



}

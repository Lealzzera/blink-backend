package com.blink.backend.controller.dashboard;

import com.blink.backend.domain.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("{clinicId}")
    public DashboardDTO getDashboard(
            @PathVariable Integer clinicId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        startDate = Objects.isNull(startDate) ? LocalDate.now().minusDays(1) : startDate;
        endDate = Objects.isNull(endDate) ? LocalDate.now() : endDate;
        return dashboardService.getDashboardData(clinicId,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay());
    }


}

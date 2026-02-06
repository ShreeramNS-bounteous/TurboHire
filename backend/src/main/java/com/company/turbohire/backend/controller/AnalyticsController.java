package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.analytics.*;
import com.company.turbohire.backend.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    // ADMIN + HR
    @GetMapping("/common")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public CommonDashboardDto common() {
        return service.getCommonDashboard();
    }

    // ADMIN ONLY
    @GetMapping("/performance")
    @PreAuthorize("hasRole('ADMIN')")
    public PerformanceDto performance(
            @RequestParam Long candidateId,
            @RequestParam Long jobId
    ) {
        return service.getPerformance(candidateId, jobId);
    }
}

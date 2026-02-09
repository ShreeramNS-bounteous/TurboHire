package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.analytics.*;
import com.company.turbohire.backend.security.util.SecurityUtils;
import com.company.turbohire.backend.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    // ADMIN + HR
    @GetMapping("/common")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public CommonDashboardDto common() {

        Long userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getCurrentUserRole();

        if ("ADMIN".equals(role)) {
            return service.getCommonDashboard();
        }

        return service.getDashboardForHr(userId);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public CommonDashboardDto filtered(
            @RequestParam(required=false) String from,
            @RequestParam(required=false) String to,
            @RequestParam(required=false) Long interviewerId,
            @RequestParam(required=false) String round) {

        Long userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getCurrentUserRole();

        LocalDate f = from != null ? LocalDate.parse(from) : null;
        LocalDate t = to != null ? LocalDate.parse(to) : null;

        return service.getFilteredStats(
                userId,
                role,
                f,
                t,
                interviewerId,
                round
        );
    }
}
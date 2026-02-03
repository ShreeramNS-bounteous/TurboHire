package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.entity.BusinessUnit;
import com.company.turbohire.backend.services.BusinessUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business-units")
@RequiredArgsConstructor
public class BusinessUnitController {

    private final BusinessUnitService businessUnitService;


    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    @PostMapping
    public Long createBU(@RequestBody BusinessUnit bu) {
        return businessUnitService.createBU(bu);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<BusinessUnit> getAllBU() {
        return businessUnitService.getAllBU();
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{buId}")
    public BusinessUnit getBU(@PathVariable Long buId) {
        return businessUnitService.getBUById(buId);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/job/{jobId}")
    public BusinessUnit getBUByJob(@PathVariable Long jobId) {
        return businessUnitService.getBUByJobId(jobId);
    }
}

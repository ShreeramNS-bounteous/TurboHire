package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.entity.BusinessUnit;
import com.company.turbohire.backend.services.BusinessUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business-units")
@RequiredArgsConstructor
public class BusinessUnitController {

    private final BusinessUnitService businessUnitService;

    // CREATE BU
    @PostMapping
    public Long createBU(@RequestBody BusinessUnit bu) {
        return businessUnitService.createBU(bu);
    }

    // GET ALL BU
    @GetMapping
    public List<BusinessUnit> getAllBU() {
        return businessUnitService.getAllBU();
    }

    // GET BU BY ID
    @GetMapping("/{buId}")
    public BusinessUnit getBU(@PathVariable Long buId) {
        return businessUnitService.getBUById(buId);
    }

    // GET BU BY JOB ID
    @GetMapping("/job/{jobId}")
    public BusinessUnit getBUByJob(@PathVariable Long jobId) {
        return businessUnitService.getBUByJobId(jobId);
    }
}

package com.company.turbohire.backend.services;

import com.company.turbohire.backend.entity.BusinessUnit;
import com.company.turbohire.backend.entity.Job;
import com.company.turbohire.backend.repository.BURepository;
import com.company.turbohire.backend.repository.JobRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessUnitService {

    private final BURepository businessUnitRepository;
    private final JobRepository jobRepository;

    // CREATE BU
    public Long createBU(BusinessUnit bu) {

        if (bu.getName() == null || bu.getName().isBlank()) {
            throw new RuntimeException("Business Unit name is mandatory");
        }

        businessUnitRepository.save(bu);
        return bu.getId();
    }

    // READ ALL BU
    @Transactional(readOnly = true)
    public List<BusinessUnit> getAllBU() {
        return businessUnitRepository.findAll();
    }

    // READ BU BY ID
    @Transactional(readOnly = true)
    public BusinessUnit getBUById(Long buId) {
        return businessUnitRepository.findById(buId)
                .orElseThrow(() -> new RuntimeException("Business Unit not found"));
    }


    @Transactional(readOnly = true)
    public BusinessUnit getBUByJobId(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return job.getBusinessUnit();
    }
}

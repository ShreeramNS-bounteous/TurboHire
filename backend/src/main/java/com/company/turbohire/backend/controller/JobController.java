package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.job.*;
import com.company.turbohire.backend.entity.BusinessUnit;
import com.company.turbohire.backend.entity.Job;
import com.company.turbohire.backend.repository.BURepository;
import com.company.turbohire.backend.security.util.SecurityUtils;
import com.company.turbohire.backend.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final BURepository buRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public JobResponse createJob(@RequestBody CreateJobRequest req) {

        Long actorUserId = SecurityUtils.getCurrentUserId();

        BusinessUnit bu = buRepository.findById(req.getBuId())
                .orElseThrow(() -> new RuntimeException("Business Unit not found"));

        Job job = Job.builder()
                .title(req.getTitle())
                .businessUnit(bu)
                .department(req.getDepartment())
                .experienceMin(req.getExperienceMin())
                .experienceMax(req.getExperienceMax())
                .location(req.getLocation())
                .build();

        return JobResponse.from(
                jobService.createJob(job, actorUserId)
        );
    }

    @PutMapping("/{jobId}/publish")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public JobResponse publishJob(@PathVariable Long jobId) {

        return JobResponse.from(
                jobService.publishJob(
                        jobId,
                        SecurityUtils.getCurrentUserId()
                )
        );
    }

    @PutMapping("/{jobId}/close")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public JobResponse closeJob(@PathVariable Long jobId) {

        return JobResponse.from(
                jobService.closeJob(
                        jobId,
                        SecurityUtils.getCurrentUserId()
                )
        );
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public void updateJob(
            @PathVariable Long jobId,
            @RequestBody UpdateJobRequest request
    ) {
        jobService.updateJob(jobId, request);
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public void deleteJob(@PathVariable Long jobId) {

        jobService.deleteJob(
                jobId,
                SecurityUtils.getCurrentUserId()
        );
    }

    // 🔥 BU-FILTERED LIST
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER','USER')")
    public List<JobResponse> getAllJobs() {
<<<<<<< HEAD

=======
>>>>>>> parent of 235c03f (Merge pull request #28 from ShreeramNS-bounteous/feature/candidate)
        return jobService.getAllJobs()
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    /**
     * READ: JOB DETAILS
     * ADMIN / HR / USER / CANDIDATE
     */
    @GetMapping("/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER','USER')")
    public JobResponse getJobById(@PathVariable Long jobId) {

        return JobResponse.from(
                jobService.getJobById(jobId)
        );
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER','USER')")
    public List<JobResponse> getJobsByStatus(@PathVariable String status) {

        return jobService.getJobsByStatus(status)
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    @GetMapping("/round/{roundName}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER','USER')")
    public List<JobResponse> getJobsByRound(@PathVariable String roundName) {

        return jobService.getJobsByRound(roundName)
                .stream()
                .map(JobResponse::from)
                .toList();
    }
}

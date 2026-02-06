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

    /**
     * CREATE JOB
     * HR / ADMIN ONLY
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public JobResponse createJob(
            @RequestBody CreateJobRequest req
    ) {

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



    /**
     * PUBLISH JOB
     * HR / ADMIN ONLY
     */
    @PutMapping("/{jobId}/publish")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public JobResponse publishJob(
            @PathVariable Long jobId
    ) {

        Long actorUserId = SecurityUtils.getCurrentUserId();

        return JobResponse.from(
                jobService.publishJob(jobId, actorUserId)
        );
    }

    /**
     * CLOSE JOB
     * HR / ADMIN ONLY
     */
    @PutMapping("/{jobId}/close")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public JobResponse closeJob(
            @PathVariable Long jobId
    ) {

        Long actorUserId = SecurityUtils.getCurrentUserId();

        return JobResponse.from(
                jobService.closeJob(jobId, actorUserId)
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
        Long actorUserId = SecurityUtils.getCurrentUserId();
        jobService.deleteJob(jobId, actorUserId);
    }



    /**
     * READ: LIST JOBS
     * ADMIN / HR / USER / CANDIDATE
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER','USER')")
    public List<JobResponse> getAllJobs() {

        Long userId = SecurityUtils.getCurrentUserId();
        String role = SecurityUtils.getCurrentUserRole();

        // 🔥 ADMIN → see all
        if ("ADMIN".equals(role)) {
            return jobService.getAllJobs()
                    .stream()
                    .map(JobResponse::from)
                    .toList();
        }

        // 🔐 HR → only own jobs
        return jobService.getJobsByCreator(userId)
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
    public JobResponse getJobById(
            @PathVariable Long jobId
    ) {
        return JobResponse.from(
                jobService.getJobById(jobId)
        );
    }

    /**
     * READ: FILTER BY STATUS
     * ADMIN / HR / USER / CANDIDATE
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER','USER')")
    public List<JobResponse> getJobsByStatus(
            @PathVariable String status
    ) {
        return jobService.getJobsByStatus(status)
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    /**
     * READ: FILTER BY ROUND
     * ADMIN / HR / USER / CANDIDATE
     */
    @GetMapping("/round/{roundName}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER','USER')")
    public List<JobResponse> getJobsByRound(
            @PathVariable String roundName
    ) {
        return jobService.getJobsByRound(roundName)
                .stream()
                .map(JobResponse::from)
                .toList();
    }
}

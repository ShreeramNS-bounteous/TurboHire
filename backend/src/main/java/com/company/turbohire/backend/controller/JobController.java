package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.job.*;
import com.company.turbohire.backend.entity.BusinessUnit;
import com.company.turbohire.backend.entity.Job;
import com.company.turbohire.backend.repository.BURepository;
import com.company.turbohire.backend.services.JobService;
import lombok.RequiredArgsConstructor;
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
     * Frontend: Job creation page
     */
    @PostMapping
    public JobResponse createJob(
            @RequestBody CreateJobRequest req,
            @RequestParam Long actorUserId
    ) {

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
     */
    @PutMapping("/{jobId}/publish")
    public JobResponse publishJob(
            @PathVariable Long jobId,
            @RequestParam Long actorUserId
    ) {
        return JobResponse.from(
                jobService.publishJob(jobId, actorUserId)
        );
    }

    /**
     * CLOSE JOB
     */
    @PutMapping("/{jobId}/close")
    public JobResponse closeJob(
            @PathVariable Long jobId,
            @RequestParam Long actorUserId
    ) {
        return JobResponse.from(
                jobService.closeJob(jobId, actorUserId)
        );
    }

    /**
     * READ
     * Frontend: Job list page
     */
    @GetMapping
    public List<JobResponse> getAllJobs() {
        return jobService.getAllJobs()
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    /**
     * READ
     * Frontend: Job details page
     */
    @GetMapping("/{jobId}")
    public JobResponse getJobById(
            @PathVariable Long jobId
    ) {
        return JobResponse.from(
                jobService.getJobById(jobId)
        );
    }

    /**
     * READ
     * Frontend: Filter jobs by status
     */
    @GetMapping("/status/{status}")
    public List<JobResponse> getJobsByStatus(
            @PathVariable String status
    ) {
        return jobService.getJobsByStatus(status)
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    /**
     * READ
     * Frontend: Filter jobs by round
     */
    @GetMapping("/round/{roundName}")
    public List<JobResponse> getJobsByRound(
            @PathVariable String roundName
    ) {
        return jobService.getJobsByRound(roundName)
                .stream()
                .map(JobResponse::from)
                .toList();
    }
}

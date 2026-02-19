package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.entity.JobRound;
import com.company.turbohire.backend.services.JobRoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/{jobId}/rounds")
@RequiredArgsConstructor
public class JobRoundController {

    private final JobRoundService jobRoundService;

    /**
     * HR creates job rounds
     */
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public Long createRound(
            @PathVariable Long jobId,
            @RequestBody JobRound request
    ) {
        return jobRoundService.createRound(
                jobId,
                request.getRoundName(),
<<<<<<< HEAD
                request.getRoundOrder()
        );
=======
                request.getRoundOrder(),
                request.getEvaluationTemplateCode() // 🔥 NEW
        );

>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
    }

    /**
     * HR views all rounds of a job
     */
    @GetMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public List<JobRound> getRounds(@PathVariable Long jobId) {
        return jobRoundService.getRoundsForJob(jobId);
    }
}

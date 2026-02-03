package com.company.turbohire.backend.services;

import com.company.turbohire.backend.entity.Job;
import com.company.turbohire.backend.entity.JobRound;
import com.company.turbohire.backend.repository.JobRepository;
import com.company.turbohire.backend.repository.JobRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobRoundService {

    private final JobRepository jobRepository;
    private final JobRoundRepository jobRoundRepository;

    /**
     * HR creates a round for a job
     */
    public Long createRound(Long jobId, String roundName, Integer roundOrder) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobRound round = JobRound.builder()
                .job(job)
                .roundName(roundName)
                .roundOrder(roundOrder)
                .build();

        jobRoundRepository.save(round);
        return round.getId();
    }

    /**
     * Get all rounds for a job (ordered)
     */
    public List<JobRound> getRoundsForJob(Long jobId) {
        return jobRoundRepository.findByJob_IdOrderByRoundOrderAsc(jobId);
    }

    /**
     * Used by Pipeline / Interview creation
     */
    public JobRound getFirstRoundForJob(Long jobId) {
        return jobRoundRepository.findFirstByJob_IdOrderByRoundOrderAsc(jobId)
                .orElseThrow(() -> new RuntimeException("No rounds configured for job"));
    }


}

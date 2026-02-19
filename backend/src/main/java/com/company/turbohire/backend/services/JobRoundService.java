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
<<<<<<< HEAD
    public Long createRound(Long jobId, String roundName, Integer roundOrder) {
=======
    public Long createRound(Long jobId,
                            String roundName,
                            Integer roundOrder,
                            String evaluationTemplateCode) {
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobRound round = JobRound.builder()
                .job(job)
                .roundName(roundName)
                .roundOrder(roundOrder)
<<<<<<< HEAD
                .build();

        jobRoundRepository.save(round);
        return round.getId();
    }

=======
                .evaluationTemplateCode(evaluationTemplateCode) // 🔥 NEW
                .build();

        jobRoundRepository.save(round);

        return round.getId();
    }


>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
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

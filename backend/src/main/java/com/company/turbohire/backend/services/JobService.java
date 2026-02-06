package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.dto.job.UpdateJobRequest;
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
public class JobService {

    private final JobRepository jobRepository;
    private final JobRoundRepository jobRoundRepository;
    private final SystemLogger systemLogger;



    // CREATE JOB
    public Job createJob(Job job, Long actorUserId) {

        job.setStatus("ON_HOLD"); // default status
        Job savedJob = jobRepository.save(job);

        // ✅ AUDIT LOG
        systemLogger.audit(actorUserId, "CREATE_JOB", "JOB", savedJob.getId());

        return savedJob;
    }

    // PUBLISH JOB
    public Job publishJob(Long jobId, Long actorUserId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!"ON_HOLD".equals(job.getStatus())) {
            throw new RuntimeException("Only ON_HOLD jobs can be published");
        }

        job.setStatus("OPEN");
        Job updatedJob = jobRepository.save(job);

        // ✅ AUDIT LOG
        systemLogger.audit(actorUserId, "PUBLISH_JOB", "JOB", jobId);

        return updatedJob;
    }

    @Transactional
    public void updateJob(Long jobId, UpdateJobRequest req) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (req.getTitle() != null)
            job.setTitle(req.getTitle());

        if (req.getLocation() != null)
            job.setLocation(req.getLocation());

        if (req.getExperienceMin() != null)
            job.setExperienceMin(req.getExperienceMin());

        if (req.getExperienceMax() != null)
            job.setExperienceMax(req.getExperienceMax());

        if (req.getStatus() != null)
            job.setStatus(req.getStatus());

        jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        jobRepository.delete(job);
    }


    // CLOSE JOB
    public Job closeJob(Long jobId, Long actorUserId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus("CLOSED");
        Job updatedJob = jobRepository.save(job);

        // ✅ AUDIT LOG
        systemLogger.audit(actorUserId, "CLOSE_JOB", "JOB", jobId);

        return updatedJob;
    }


    // READ - JOB BY ID (Frontend mandatory)
    @Transactional(readOnly = true)
    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    // READ - ALL JOBS (Frontend mandatory)
    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // READ - JOBS BY STATUS (Frontend)
    @Transactional(readOnly = true)
    public List<Job> getJobsByStatus(String status) {
        return jobRepository.findByStatus(status);
    }
    @Transactional(readOnly = true)
    public List<Job> getJobsByRound(String roundName) {

        List<JobRound> rounds = jobRoundRepository.findByRoundName(roundName);

        return rounds.stream()
                .map(JobRound::getJob)
                .distinct()
                .toList();
    }

}

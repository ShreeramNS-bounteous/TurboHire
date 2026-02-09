package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.dto.job.UpdateJobRequest;
import com.company.turbohire.backend.entity.Job;
import com.company.turbohire.backend.entity.JobRound;
import com.company.turbohire.backend.repository.JobRepository;
import com.company.turbohire.backend.repository.JobRoundRepository;
import com.company.turbohire.backend.security.util.SecurityUtils;
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

    // CREATE JOB (NO CHANGE)
    public Job createJob(Job job, Long actorUserId) {

        job.setStatus("ON_HOLD");
        Job savedJob = jobRepository.save(job);

        systemLogger.audit(actorUserId, "CREATE_JOB", "JOB", savedJob.getId());

        return savedJob;
    }

    // PUBLISH JOB (BU SAFETY)
    public Job publishJob(Long jobId, Long actorUserId) {

        Job job = jobRepository
                .findByIdAndBusinessUnit_Id(
                        jobId,
                        SecurityUtils.getCurrentBU() // 🔑 BU FILTER
                )
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!"ON_HOLD".equals(job.getStatus())) {
            throw new RuntimeException("Only ON_HOLD jobs can be published");
        }

        job.setStatus("OPEN");
        jobRepository.save(job);

        systemLogger.audit(actorUserId, "PUBLISH_JOB", "JOB", jobId);

        return job;
    }

    // UPDATE JOB (BU SAFETY)
    public void updateJob(Long jobId, UpdateJobRequest req) {

        Job job = jobRepository
                .findByIdAndBusinessUnit_Id(
                        jobId,
                        SecurityUtils.getCurrentBU() // 🔑 BU FILTER
                )
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (req.getTitle() != null) job.setTitle(req.getTitle());
        if (req.getLocation() != null) job.setLocation(req.getLocation());
        if (req.getExperienceMin() != null) job.setExperienceMin(req.getExperienceMin());
        if (req.getExperienceMax() != null) job.setExperienceMax(req.getExperienceMax());
        if (req.getStatus() != null) job.setStatus(req.getStatus());

        jobRepository.save(job);
    }

    // SOFT DELETE (BU SAFETY)
    public void deleteJob(Long jobId, Long actorUserId) {

        Job job = jobRepository
                .findByIdAndBusinessUnit_Id(
                        jobId,
                        SecurityUtils.getCurrentBU() // 🔑 BU FILTER
                )
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus("DELETED");
        jobRepository.save(job);

        systemLogger.audit(actorUserId, "DELETE_JOB", "JOB", jobId);
    }

    // CLOSE JOB (BU SAFETY)
    public Job closeJob(Long jobId, Long actorUserId) {

        Job job = jobRepository
                .findByIdAndBusinessUnit_Id(
                        jobId,
                        SecurityUtils.getCurrentBU() // 🔑 BU FILTER
                )
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus("CLOSED");
        jobRepository.save(job);

        systemLogger.audit(actorUserId, "CLOSE_JOB", "JOB", jobId);

        return job;
    }

    // READ - JOB BY ID (BU FILTER)
    @Transactional(readOnly = true)
    public Job getJobById(Long jobId) {

        return jobRepository
                .findByIdAndBusinessUnit_Id(
                        jobId,
                        SecurityUtils.getCurrentBU() // 🔑 BU FILTER
                )
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    // READ - ALL JOBS (🔥 MAIN FIX)
    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {

        return jobRepository.findByBusinessUnit_IdAndStatusNot(
                SecurityUtils.getCurrentBU(), // 🔑 BU FILTER
                "DELETED"
        );
    }

    // READ - JOBS BY STATUS (BU FILTER)
    @Transactional(readOnly = true)
    public List<Job> getJobsByStatus(String status) {

        return jobRepository.findByBusinessUnit_IdAndStatus(
                SecurityUtils.getCurrentBU(), // 🔑 BU FILTER
                status
        );
    }

    // READ - JOBS BY ROUND (BU FILTER)
    @Transactional(readOnly = true)
    public List<Job> getJobsByRound(String roundName) {

        List<JobRound> rounds = jobRoundRepository.findByRoundName(roundName);

        return rounds.stream()
                .map(JobRound::getJob)
                .filter(job ->
                        job.getBusinessUnit().getId()
                                .equals(SecurityUtils.getCurrentBU()) // 🔑 BU FILTER
                )
                .filter(job -> !"DELETED".equals(job.getStatus()))
                .distinct()
                .toList();
    }
}

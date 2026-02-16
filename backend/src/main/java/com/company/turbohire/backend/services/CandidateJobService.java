package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.*;
import com.company.turbohire.backend.enums.CandidateLockStatus;
import com.company.turbohire.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateJobService {

    private final CandidateJobRepository candidateJobRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final BURepository businessUnitRepository;
    private final CandidateLockRepository candidateLockRepository;
    private final PipelineStageHistoryRepository pipelineStageHistoryRepository;
    private final CandidatePortalTokenRepository tokenRepository;
    private final SystemLogger systemLogger;

    // ===============================
    // ADD CANDIDATE TO PIPELINE
    // ===============================
    public Long addCandidateToPipeline(
            Long candidateId,
            Long jobId,
            Long buId,
            Long actorUserId
    ) {

        // 1️⃣ Validate input early (IMPORTANT)
        if (candidateId == null || jobId == null || buId == null) {
            throw new IllegalArgumentException("candidateId, jobId, buId must not be null");
        }

        // 2️⃣ Check candidate lock
        candidateLockRepository.findActiveLockByCandidateId(candidateId)
                .ifPresent(l -> {
                    throw new RuntimeException("Candidate already locked in another pipeline");
                });

        // 3️⃣ Fetch entities
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        BusinessUnit bu = businessUnitRepository.findById(buId)
                .orElseThrow(() -> new RuntimeException("Business Unit not found"));

        // 4️⃣ Create CandidateJob (PIPELINE ENTRY)
        CandidateJob cj = CandidateJob.builder()
                .candidate(candidate)
                .job(job)
                .businessUnit(bu)
                .currentStage("SHORTLISTED")
                .status("IN_PROGRESS")
                .build();

        candidateJobRepository.save(cj);

        // 5️⃣ Lock candidate for this job + BU
        CandidateLock lock = CandidateLock.builder()
                .candidate(candidate)
                .lockedJob(job)
                .lockedBusinessUnit(bu)
                .lockStatus(CandidateLockStatus.LOCKED)
                .build();

        candidateLockRepository.save(lock);

        // 6️⃣ Save pipeline history
        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        cj.getId(),
                        null,
                        "SHORTLISTED",
                        actorUserId
                )
        );

        // 7️⃣ Audit logs
        systemLogger.audit(
                actorUserId,
                "PIPELINE_ENTRY",
                "CANDIDATE_JOB",
                cj.getId()
        );

        systemLogger.hiringEvent(
                candidateId,
                jobId,
                buId,
                "PIPELINE_ENTRY"
        );

        // 8️⃣ Generate candidate portal token (for future use)
        String token = UUID.randomUUID().toString();

        CandidatePortalToken portalToken = CandidatePortalToken.builder()
                .token(token)
                .candidateJob(cj)
                .build();

        tokenRepository.save(portalToken);

        System.out.println(
                "📩 Candidate Portal URL: http://localhost:8080/api/candidate-portal?token=" + token
        );

        return cj.getId();
    }

    // ===============================
    // MOVE PIPELINE STAGE
    // ===============================
    public void moveStage(Long candidateJobId, String nextStage, Long actorUserId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        String prevStage = cj.getCurrentStage();
        cj.setCurrentStage(nextStage);

        candidateJobRepository.save(cj);

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        candidateJobId,
                        prevStage,
                        nextStage,
                        actorUserId
                )
        );
    }

    // ===============================
    // REJECT CANDIDATE
    // ===============================


    // ===============================
    // READ APIs
    // ===============================
    @Transactional(readOnly = true)
    public CandidateJob getCandidateJob(Long candidateJobId) {
        return candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));
    }

    @Transactional(readOnly = true)
    public List<CandidateJob> getCandidateJobs(Long candidateId) {
        return candidateJobRepository.findByCandidate_Id(candidateId);
    }

    @Transactional(readOnly = true)
    public List<CandidateJob> getActiveCandidatesByStage(String stage) {
        return candidateJobRepository.findByCurrentStageAndStatus(
                stage,
                "IN_PROGRESS"
        );
    }
}

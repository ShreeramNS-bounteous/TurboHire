package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.*;
import com.company.turbohire.backend.enums.CandidateLockStatus;
import com.company.turbohire.backend.enums.InterviewStatus;
import com.company.turbohire.backend.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.company.turbohire.backend.notification.service.NotificationService;


import java.util.List;
import java.util.Map;
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
    private final SystemLogger systemLogger;
    private final CandidatePortalTokenRepository tokenRepository;
    private final JobRoundRepository jobRoundRepository;
    private final InterviewRepository interviewRepository;
    private final NotificationService notificationService;


    // WRITE (candidate is already shortlisted externally)
    public Long addCandidateToPipeline(Long candidateId, Long jobId, Long buId, Long actorUserId) {

        candidateLockRepository.findActiveLockByCandidateId(candidateId)
                .ifPresent(l -> { throw new RuntimeException("Candidate locked"); });

        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow();
        Job job = jobRepository.findById(jobId).orElseThrow();
        BusinessUnit bu = businessUnitRepository.findById(buId).orElseThrow();

        CandidateJob cj = CandidateJob.builder()
                .candidate(candidate)
                .job(job)
                .businessUnit(bu)
                .currentStage("SHORTLISTED")
                .status("IN_PROGRESS")
                .build();

        candidateJobRepository.save(cj);

        JobRound firstRound = jobRoundRepository
                .findByJob_Id(jobId)
                .stream()
                .filter(r -> r.getRoundOrder() == 1)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No first round defined for job")
                );

        Interview interview = Interview.builder()
                .candidateJob(cj)
                .round(firstRound)
                .status(InterviewStatus.SCHEDULED)
                .build();

        interviewRepository.save(interview);

        systemLogger.audit(
                actorUserId,
                "INTERVIEW_CREATED",
                "Interview",
                interview.getId(),
                Map.of(
                        "roundId", firstRound.getId(),
                        "candidateJobId", cj.getId()
                )
        );

        CandidateLock lock = CandidateLock.builder()
                .candidate(candidate)
                .lockedJob(job)
                .lockedBusinessUnit(bu)
                .lockStatus(CandidateLockStatus.LOCKED)
                .build();

        candidateLockRepository.save(lock);

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(cj.getId(), null, "SHORTLISTED", actorUserId)
        );

        systemLogger.audit(actorUserId, "PIPELINE_ENTRY", "CANDIDATE_JOB", cj.getId());
        systemLogger.hiringEvent(candidateId, jobId, buId, "PIPELINE_ENTRY");

        systemLogger.audit(
                actorUserId,
                "CANDIDATE_ADDED",
                "CandidateJob",
                cj.getId(),
                Map.of(
                        "jobId", jobId,
                        "buId", buId,
                        "stage", "SHORTLISTED"
                )
        );

        String token = UUID.randomUUID().toString();

        CandidatePortalToken portalToken = CandidatePortalToken.builder()
                .token(token)
                .candidateJob(cj)
                .build();

        tokenRepository.save(portalToken);

        // ============================
        // 📩 LOG PORTAL URL (LOCAL)
        // ============================
        String portalUrl =
                "http://localhost:8080/api/candidate-portal?token=" + token;

        System.out.println("📩 Candidate Portal URL: " + portalUrl);


        return cj.getId();
    }

    public void moveStage(Long candidateJobId, String nextStage, Long actorUserId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId).orElseThrow();
        String prevStage = cj.getCurrentStage();

        cj.setCurrentStage(nextStage);
        candidateJobRepository.save(cj);

        // 🔥 ONLY CANDIDATE MAIL
        String token =
                tokenRepository.findByCandidateJob(cj)
                        .orElseThrow(() -> new RuntimeException("Portal token not found"))
                        .getToken();

        String portalLink =
                "http://localhost:8080/api/candidate-portal?token=" + token;


        notificationService.notifyCandidateStatus(
                cj.getCandidate(),
                portalLink,
                nextStage
        );
        systemLogger.audit(
                actorUserId,
                "STAGE_CHANGED",
                "CandidateJob",
                candidateJobId,
                Map.of(
                        "oldStage", prevStage,
                        "newStage", nextStage
                )
        );

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        candidateJobId,
                        prevStage,
                        nextStage,
                        actorUserId
                )
        );
    }

    public void reject(Long candidateJobId, Long actorUserId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId).orElseThrow();
        String prev = cj.getCurrentStage();

        cj.setCurrentStage("REJECTED");
        cj.setStatus("REJECTED");
        candidateJobRepository.save(cj);

        candidateLockRepository.releaseLock(cj.getCandidate().getId());

        // 🔥 ONLY CANDIDATE
        String token =
                tokenRepository.findByCandidateJob(cj)
                        .orElseThrow(() -> new RuntimeException("Portal token not found"))
                        .getToken();

        String portalLink =
                "http://localhost:8080/api/candidate-portal?token=" + token;


        systemLogger.audit(
                actorUserId,
                "CANDIDATE_REJECTED",
                "CandidateJob",
                candidateJobId,
                Map.of("previousStage", prev)
        );

        systemLogger.hiringEvent(
                cj.getCandidate().getId(),
                cj.getJob().getId(),
                cj.getBusinessUnit().getId(),
                "CANDIDATE_REJECTED"
        );

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        candidateJobId,
                        prev,
                        "REJECTED",
                        actorUserId
                )
        );
    }

    // READ
    public CandidateJob getCandidateJob(Long candidateJobId) {
        return candidateJobRepository.findById(candidateJobId).orElseThrow();
    }

    public List<CandidateJob> getCandidateJobs(Long candidateId) {
        return candidateJobRepository.findByCandidate_Id(candidateId);
    }

    public List<PipelineStageHistory> getPipelineHistory(Long candidateJobId) {
        return pipelineStageHistoryRepository.findByCandidateJobId(candidateJobId);
    }

    @Transactional(readOnly = true)
    public List<CandidateJob> getActiveCandidatesByStage(String stage) {

        return candidateJobRepository.findByCurrentStageAndStatus(
                stage,
                "IN_PROGRESS"
        );
    }
}

package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.*;
import com.company.turbohire.backend.enums.CandidateLockStatus;
import com.company.turbohire.backend.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        return cj.getId();
    }

    public void moveStage(Long candidateJobId, String nextStage, Long actorUserId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId).orElseThrow();
        String prev = cj.getCurrentStage();

        cj.setCurrentStage(nextStage);
        candidateJobRepository.save(cj);

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(candidateJobId, prev, nextStage, actorUserId)
        );
    }

    public void reject(Long candidateJobId, Long actorUserId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId).orElseThrow();
        String prev = cj.getCurrentStage();

        cj.setCurrentStage("REJECTED");
        cj.setStatus("REJECTED");
        candidateJobRepository.save(cj);

        candidateLockRepository.releaseLock(cj.getCandidate().getId());

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(candidateJobId, prev, "REJECTED", actorUserId)
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

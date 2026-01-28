package com.company.turbohire.backend.service;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.enums.CandidateLockStatus;
import com.company.turbohire.backend.repository.CandidateJobRepository;
import com.company.turbohire.backend.repository.CandidateLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferService {

    private final CandidateJobRepository candidateJobRepository;
    private final CandidateLockRepository candidateLockRepository;
    private final SystemLogger systemLogger;

    // WRITE
    public void releaseOffer(Long candidateJobId, Double ctc, Long actorUserId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        if ("RELEASED".equals(cj.getOfferStatus())) {
            throw new RuntimeException("Offer already released");
        }

        cj.setOfferCtc(ctc);
        cj.setOfferStatus("RELEASED");
        candidateJobRepository.save(cj);

        // ✅ AUDIT (MANDATORY)
        systemLogger.audit(actorUserId, "RELEASE_OFFER", "CANDIDATE_JOB", candidateJobId);
        systemLogger.hiringEvent(
                cj.getCandidate().getId(),
                cj.getJob().getId(),
                cj.getBusinessUnit().getId(),
                "OFFER_RELEASED"
        );
    }

    public void acceptOffer(Long candidateJobId, Long actorUserId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        if (!"RELEASED".equals(cj.getOfferStatus())) {
            throw new RuntimeException("Offer not released yet");
        }

        cj.setOfferStatus("ACCEPTED");
        candidateJobRepository.save(cj);

        // ✅ release candidate lock (NO custom repo method)
        candidateLockRepository.findActiveLockByCandidateId(cj.getCandidate().getId())
                .ifPresent(lock -> {
                    lock.setLockStatus(CandidateLockStatus.RELEASED);
                    lock.setReleasedAt(LocalDateTime.now());
                });

        // ✅ AUDIT (MANDATORY)
        systemLogger.audit(actorUserId, "ACCEPT_OFFER", "CANDIDATE_JOB", candidateJobId);
        systemLogger.hiringEvent(
                cj.getCandidate().getId(),
                cj.getJob().getId(),
                cj.getBusinessUnit().getId(),
                "OFFER_ACCEPTED"
        );
    }

    // READ
    public CandidateJob getOfferDetails(Long candidateJobId) {
        return candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));
    }
}

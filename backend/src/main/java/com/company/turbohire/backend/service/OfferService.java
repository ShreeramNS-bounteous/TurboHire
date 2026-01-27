package com.company.turbohire.backend.service;

import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.CandidateLock;
import com.company.turbohire.backend.enums.CandidateLockStatus;
import com.company.turbohire.backend.repository.CandidateJobRepository;
import com.company.turbohire.backend.repository.CandidateLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferService {

    private final CandidateJobRepository candidateJobRepository;
    private final CandidateLockRepository candidateLockRepository;

    // WRITE
    public void releaseOffer(Long candidateJobId, Double ctc) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId).orElseThrow();
        cj.setOfferCtc(ctc);
        cj.setOfferStatus("RELEASED");

        candidateJobRepository.save(cj);
    }

    public void acceptOffer(Long candidateJobId) {

        CandidateJob cj = candidateJobRepository.findById(candidateJobId).orElseThrow();
        cj.setOfferStatus("ACCEPTED");
        candidateJobRepository.save(cj);

        // ✅ release candidate lock (NO custom repo method)
        candidateLockRepository.findActiveLockByCandidateId(cj.getCandidate().getId())
                .ifPresent(lock -> {
                    lock.setLockStatus(CandidateLockStatus.RELEASED);
                    lock.setReleasedAt(java.time.LocalDateTime.now());
                    candidateLockRepository.save(lock);
                });
    }

    // READ
    public CandidateJob getOfferDetails(Long candidateJobId) {
        return candidateJobRepository.findById(candidateJobId).orElseThrow();
    }
}

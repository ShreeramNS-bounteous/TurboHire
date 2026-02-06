package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.CandidatePortalToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatePortalTokenRepository
        extends JpaRepository<CandidatePortalToken, Long> {

    Optional<CandidatePortalToken> findByToken(String token);
    Optional<CandidatePortalToken> findByCandidateJob(CandidateJob candidateJob);

}


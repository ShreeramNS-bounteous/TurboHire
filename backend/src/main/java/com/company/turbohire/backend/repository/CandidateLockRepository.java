package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface CandidateLockRepository extends JpaRepository<CandidateLock, Long> {

    // check global lock
    Optional<CandidateLock> findActiveLockByCandidateId(Long candidateId);

    // release lock on reject / offer accept
    @Modifying
    void releaseLock(Long candidateId);
}

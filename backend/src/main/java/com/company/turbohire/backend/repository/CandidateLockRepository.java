package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateLock;
import com.company.turbohire.backend.enums.CandidateLockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CandidateLockRepository extends JpaRepository<CandidateLock, Long> {

    // supports: findActiveLockByCandidateId(candidateId)
    @Query("""
        SELECT cl
        FROM CandidateLock cl
        WHERE cl.candidate.id = :candidateId
          AND cl.lockStatus = com.company.turbohire.backend.enums.CandidateLockStatus.LOCKED
    """)
    Optional<CandidateLock> findActiveLockByCandidateId(@Param("candidateId") Long candidateId);

    // supports: releaseLock(candidateId)
    @Modifying
    @Transactional
    @Query("""
        UPDATE CandidateLock cl
        SET cl.lockStatus = com.company.turbohire.backend.enums.CandidateLockStatus.RELEASED
        WHERE cl.candidate.id = :candidateId
    """)
    void releaseLock(@Param("candidateId") Long candidateId);
}

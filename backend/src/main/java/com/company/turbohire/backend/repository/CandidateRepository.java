package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    @Query("""
    SELECT c FROM Candidate c
    WHERE 
        NOT EXISTS (
            SELECT cj FROM CandidateJob cj
            WHERE cj.candidate = c
            AND cj.status IN( 'IN_PROGRESS','HIRED')
        )
    AND 
        NOT EXISTS (
            SELECT cj2 FROM CandidateJob cj2
            WHERE cj2.candidate = c
            AND cj2.job.id = :jobId
        )
""")
    List<Candidate> findAvailableCandidatesForJob(@Param("jobId") Long jobId);
}

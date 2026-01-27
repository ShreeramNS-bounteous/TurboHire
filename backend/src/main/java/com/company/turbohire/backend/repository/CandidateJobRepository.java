package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateJobRepository extends JpaRepository<CandidateJob, Long> {

    // READ for frontend
    List<CandidateJob> findByCandidate_Id(Long candidateId);
}

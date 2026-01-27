package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateJobRepository extends JpaRepository<CandidateJob,Long> {
}

package com.company.turbohire.backend.repos;

import com.company.turbohire.backend.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {
}

package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {

}

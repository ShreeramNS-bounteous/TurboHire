package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.JobRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRoundRepository extends JpaRepository<JobRound, Long> {
}

package com.company.turbohire.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.turbohire.backend.entity.JobRound;

@Repository
public interface JobRoundRepository extends JpaRepository<JobRound, Long> {
    List<JobRound> findByRoundName(String roundName);
}

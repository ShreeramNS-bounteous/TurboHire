package com.company.turbohire.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.turbohire.backend.entity.JobRound;

@Repository
public interface JobRoundRepository extends JpaRepository<JobRound, Long> {

    // keep this if you want (not harmful)
    List<JobRound> findByRoundName(String roundName);

    // ✅ REQUIRED for pipeline & interview flow
    List<JobRound> findByJob_IdOrderByRoundOrderAsc(Long jobId);

    // ✅ Used when creating interview
    Optional<JobRound> findFirstByJob_IdOrderByRoundOrderAsc(Long jobId);

    List<JobRound> findByJob_Id(Long jobId);


}


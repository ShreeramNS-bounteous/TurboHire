package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.JobRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    boolean existsByCandidateJobAndRound(CandidateJob candidateJob, JobRound jobRound);
}

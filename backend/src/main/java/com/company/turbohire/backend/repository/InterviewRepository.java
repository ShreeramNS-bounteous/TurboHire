package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    boolean existsByCandidateJobAndRound(CandidateJob candidateJob, com.company.turbohire.backend.entity.JobRound jobRound);

    List<Interview> findByCandidateJob_Candidate_Id(Long candidateId);

    List<Interview> findByCandidateJob_Job_Id(Long jobId);

    @Query("""
  SELECT i FROM Interview i
  WHERE i.status='COMPLETED'
  AND NOT EXISTS (
     SELECT f FROM InterviewFeedback f
     WHERE f.interview.id = i.id
  )
""")
    List<Interview> findPendingFeedback();
}

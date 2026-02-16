package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.Job;
import com.company.turbohire.backend.entity.JobRound;
import com.company.turbohire.backend.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    boolean existsByCandidateJobAndRound(CandidateJob candidateJob, com.company.turbohire.backend.entity.JobRound jobRound);

    List<Interview> findByCandidateJob_Candidate_Id(Long candidateId);

    List<Interview> findByCandidateJob_Job_Id(Long jobId);

    boolean existsByCandidateJob_IdAndRound_RoundNameAndStatusIn(
            Long candidateJobId,
            String roundName,
            List<InterviewStatus> statuses
    );


    Optional<JobRound> findFirstByCandidateJob_Job_IdOrderByRound_RoundOrderAsc(Long jobId);

    List<Interview> findByStatus(InterviewStatus status);







//    @Query("""
//        SELECT i
//        FROM Interview i
//        JOIN InterviewAssignment ia ON ia.interview = i
//        WHERE ia.interviewer.id = :interviewerId
//          AND NOT EXISTS (
//              SELECT 1
//              FROM InterviewFeedback f
//              WHERE f.interview = i
//                AND f.interviewer.id = :interviewerId
//          )
//    """)
//    List<Interview> findPendingFeedbackInterviews(Long interviewerId);
}

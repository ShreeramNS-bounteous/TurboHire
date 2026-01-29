package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.InterviewFeedback;
import com.company.turbohire.backend.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewFeedbackRepository extends JpaRepository<InterviewFeedback, Long> {

    boolean existsByInterview_IdAndInterviewer_Id(Long interviewId, Long interviewerUserId);

    List<InterviewFeedback> findByInterview_CandidateJobAndInterview_Round_RoundOrderLessThan(
            CandidateJob candidateJob, Integer roundOrder);

    List<InterviewFeedback> findByInterview_CandidateJob_Candidate_Id(Long candidateId);



//    @Query("""
//            SELECT f FROM InterviewFeedback f
//            WHERE f.interviewer.id = :interviewerId
//            AND f.interview.status = 'SCHEDULED'
//            AND f.id IS NULL
//            """)
//    List<InterviewFeedback> findPendingFeedbackByInterviewer(Long interviewerId);
}

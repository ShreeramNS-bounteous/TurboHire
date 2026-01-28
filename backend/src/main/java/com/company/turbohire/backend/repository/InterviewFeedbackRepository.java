package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.InterviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewFeedbackRepository
        extends JpaRepository<InterviewFeedback, Long> {

    boolean existsByInterview_IdAndInterviewer_Id(
            Long interviewId,
            Long interviewerUserId
    );

    // ⭐ required for previous-round feedback
    List<InterviewFeedback>
    findByInterview_CandidateJobAndInterview_Round_RoundOrderLessThan(
            CandidateJob candidateJob,
            Integer roundOrder
    );
}
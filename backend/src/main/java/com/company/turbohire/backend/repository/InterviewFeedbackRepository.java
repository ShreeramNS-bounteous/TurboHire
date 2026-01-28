package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateJob;

import com.company.turbohire.backend.entity.InterviewFeedback;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface InterviewFeedbackRepository

        extends JpaRepository<InterviewFeedback, Long> {

    // Prevent duplicate feedback by same interviewer for same interview

    boolean existsByInterview_IdAndInterviewer_Id(

            Long interviewId,

            Long interviewerUserId

    );

    // 🔹 Interviewer view: previous round feedback

    List<InterviewFeedback>

    findByInterview_CandidateJobAndInterview_Round_RoundOrderLessThan(

            CandidateJob candidateJob,

            Integer roundOrder

    );

    //HR view: ALL feedback for a candidate

    List<InterviewFeedback>

    findByInterview_CandidateJob_Candidate_Id(Long candidateId);

}


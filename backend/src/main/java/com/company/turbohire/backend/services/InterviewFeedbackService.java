package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.dto.interviewFeedback.SubmitFeedbackRequestDto;
import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.InterviewFeedback;
import com.company.turbohire.backend.entity.User;
import com.company.turbohire.backend.enums.DecisionStatus;
import com.company.turbohire.backend.enums.InterviewStatus;
import com.company.turbohire.backend.repository.InterviewFeedbackRepository;
import com.company.turbohire.backend.repository.InterviewRepository;
import com.company.turbohire.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewFeedbackService {

    private final InterviewFeedbackRepository feedbackRepository;
    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final SystemLogger systemLogger;

    /**
     * Submit feedback for an interview
     */
    @Transactional
    public void submitFeedback(Long interviewId,
                               SubmitFeedbackRequestDto request,
                               Long actorUserId) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        if (interview.getStatus() != InterviewStatus.COMPLETED) {
            throw new RuntimeException("Interview must be completed before feedback");
        }

        User interviewer = userRepository.findById(actorUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InterviewFeedback feedback = InterviewFeedback.builder()
                .interview(interview)
                .interviewer(interviewer)
                .rating(request.getRating())
                .recommendation(request.getRecommendation())
                .comments(request.getComments())
                .build();

        feedbackRepository.save(feedback);

        interview.setFeedbackSubmitted(true);
        interview.setDecisionStatus(DecisionStatus.PENDING_DECISION);
        interviewRepository.save(interview);
    }

    /**
     * Get feedback from previous rounds for the same candidate
     */
    @Transactional(readOnly = true)
    public List<InterviewFeedback> getPreviousRoundFeedback(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        return feedbackRepository.findByInterview_CandidateJobAndInterview_Round_RoundOrderLessThan(
                interview.getCandidateJob(),
                interview.getRound().getRoundOrder()
        );
    }

    /**
     * Get all feedback for a candidate
     */
    @Transactional(readOnly = true)
    public List<InterviewFeedback> getFeedbackForCandidate(Long candidateId) {
        return feedbackRepository.findByInterview_CandidateJob_Candidate_Id(candidateId);
    }

    /**
     * Get all feedback for a specific interview
     */
//    @Transactional(readOnly = true)
//    public List<InterviewFeedback> getFeedbackForInterview(Long interviewId) {
//        return feedbackRepository.findByInterview_Id(interviewId);
//    }

    /**
     * Get pending feedback for a specific interviewer
     * (interviews where this interviewer is assigned but feedback not yet submitted)
     */
//    @Transactional(readOnly = true)
//    public List<InterviewFeedback> getPendingFeedback(Long interviewerUserId) {
//        // Implemented using a custom repository method
//        return feedbackRepository.findPendingFeedbackByInterviewer(interviewerUserId);
//    }
}

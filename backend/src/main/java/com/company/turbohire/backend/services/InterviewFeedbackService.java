package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.InterviewFeedback;
import com.company.turbohire.backend.entity.User;
import com.company.turbohire.backend.repository.InterviewFeedbackRepository;
import com.company.turbohire.backend.repository.InterviewRepository;
import com.company.turbohire.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    public InterviewFeedback submitFeedback(Long interviewId, Long interviewerUserId,
                                            Integer rating, String recommendation, String comments,
                                            Long actorUserId) {

        if (feedbackRepository.existsByInterview_IdAndInterviewer_Id(interviewId, interviewerUserId)) {
            throw new RuntimeException("Feedback already submitted for this interview by this interviewer");
        }

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        User interviewer = userRepository.findById(interviewerUserId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));

        InterviewFeedback feedback = InterviewFeedback.builder()
                .interview(interview)
                .interviewer(interviewer)
                .rating(rating)
                .recommendation(recommendation)
                .comments(comments)
                .build();

        InterviewFeedback saved = feedbackRepository.save(feedback);

        // Audit log
//        systemLogger.audit(actorUserId, "SUBMIT_FEEDBACK", "INTERVIEW_FEEDBACK", saved.getId());

        systemLogger.audit(
                actorUserId,
                "FEEDBACK_SUBMITTED",
                "InterviewFeedback",
                feedback.getId(),
                Map.of(
                        "rating", feedback.getRating(),
                        "recommendation", feedback.getRecommendation(),
                        "interviewId", interviewId
                )
        );

        return saved;
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

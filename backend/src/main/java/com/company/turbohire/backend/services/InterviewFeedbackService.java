package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.InterviewFeedback;
import com.company.turbohire.backend.entity.JobRound;
import com.company.turbohire.backend.entity.User;
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
     * WRITE
     * Interviewer submits feedback for an interview
     * One feedback per interviewer per interview
     */
    public InterviewFeedback submitFeedback(
            Long interviewId,
            Long interviewerUserId,
            Integer rating,
            String recommendation,
            String comments,
            Long actorUserId
    ) {

        if (feedbackRepository.existsByInterview_IdAndInterviewer_Id(
                interviewId, interviewerUserId)) {
            throw new RuntimeException(
                    "Feedback already submitted for this interview by this interviewer");
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

        // ✅ AUDIT LOG
        systemLogger.audit(actorUserId, "SUBMIT_FEEDBACK", "INTERVIEW_FEEDBACK", saved.getId());

        return saved;

    }

    /**
     * READ
     * Interviewer view – fetch feedback from previous rounds only
     */
    @Transactional(readOnly = true)
    public List<InterviewFeedback> getPreviousRoundFeedback(Long interviewId) {

        Interview currentInterview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        JobRound currentRound = currentInterview.getRound();

        return feedbackRepository
                .findByInterview_CandidateJobAndInterview_Round_RoundOrderLessThan(
                        currentInterview.getCandidateJob(),
                        currentRound.getRoundOrder()
                );
    }

    /**
     * READ
     * HR view – fetch ALL feedback for a candidate across all rounds
     */
    @Transactional(readOnly = true)
    public List<InterviewFeedback> getAllFeedbackForCandidate(Long candidateId) {

        return feedbackRepository
                .findByInterview_CandidateJob_Candidate_Id(candidateId);
    }
}
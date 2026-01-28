package com.company.turbohire.backend.service;
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

    public InterviewFeedback submitFeedback(Long interviewId,
                                            Long interviewerUserId,
                                            Integer rating,
                                            String recommendation,
                                            String comments) {

        if (feedbackRepository.existsByInterview_IdAndInterviewer_Id(
                interviewId, interviewerUserId)) {
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
        return feedbackRepository.save(feedback);

    }

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

}


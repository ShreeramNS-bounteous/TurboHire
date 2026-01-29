package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.interviewFeedback.InterviewFeedbackResponseDto;
import com.company.turbohire.backend.dto.interviewFeedback.PendingInterviewResponseDto;
import com.company.turbohire.backend.dto.interviewFeedback.SubmitInterviewFeedbackRequestDto;
import com.company.turbohire.backend.entity.InterviewFeedback;
import com.company.turbohire.backend.services.InterviewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interviews/feedback")
@RequiredArgsConstructor
public class InterviewFeedbackController {

    private final InterviewFeedbackService feedbackService;

    /**
     * Submit feedback for an interview
     */
    @PostMapping
    public ResponseEntity<InterviewFeedbackResponseDto> submitFeedback(
            @RequestBody SubmitInterviewFeedbackRequestDto request,
            @RequestParam("actorUserId") Long actorUserId
    ) {
        InterviewFeedback feedback = feedbackService.submitFeedback(
                request.getInterviewId(),
                request.getInterviewerUserId(),
                request.getRating(),
                request.getRecommendation(),
                request.getComments(),
                actorUserId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(feedback));
    }

    /**
     * Get previous round feedback for an interview
     */
    @GetMapping("/interview/{id}/previous")
    public ResponseEntity<List<InterviewFeedbackResponseDto>> getPreviousRoundFeedback(
            @PathVariable("id") Long interviewId
    ) {
        List<InterviewFeedback> feedbacks = feedbackService.getPreviousRoundFeedback(interviewId);
        List<InterviewFeedbackResponseDto> dtos = feedbacks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get all feedback for a candidate
     */
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<InterviewFeedbackResponseDto>> getAllFeedbackForCandidate(
            @PathVariable Long candidateId
    ) {
        List<InterviewFeedback> feedbacks =
                feedbackService.getFeedbackForCandidate(candidateId);

        List<InterviewFeedbackResponseDto> dtos = feedbacks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


//    /**
//     * Get all feedback for a specific interview
//     */
//    @GetMapping("/interview/{id}")
//    public ResponseEntity<List<InterviewFeedbackResponseDto>> getFeedbackForInterview(
//            @PathVariable("id") Long interviewId
//    ) {
//        List<InterviewFeedback> feedbacks = feedbackService.getFeedbackForInterview(interviewId);
//        List<InterviewFeedbackResponseDto> dtos = feedbacks.stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(dtos);
//    }

    /**
     * Get pending feedback for an interviewer
     */
//    @GetMapping("/pending")
//    public ResponseEntity<List<PendingInterviewResponseDto>> getPendingFeedback(
//            @RequestParam("interviewerId") Long interviewerUserId
//    ) {
//        List<PendingInterviewResponseDto> pendingList = feedbackService.getPendingFeedback(interviewerUserId);
//        return ResponseEntity.ok(pendingList);
//    }

    // ------------------ Mapping helpers ------------------

    private InterviewFeedbackResponseDto mapToDto(InterviewFeedback feedback) {
        return InterviewFeedbackResponseDto.builder()
                .id(feedback.getId())
                .interviewId(feedback.getInterview().getId())
                .interviewerId(feedback.getInterviewer().getId())
                .rating(feedback.getRating())
                .recommendation(feedback.getRecommendation())
                .comments(feedback.getComments())
                .submittedAt(feedback.getSubmittedAt())
                .build();
    }
}

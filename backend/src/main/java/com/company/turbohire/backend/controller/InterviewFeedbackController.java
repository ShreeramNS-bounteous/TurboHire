package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.interviewFeedback.InterviewFeedbackResponseDto;
import com.company.turbohire.backend.dto.interviewFeedback.PendingInterviewResponseDto;
import com.company.turbohire.backend.dto.interviewFeedback.SubmitFeedbackRequestDto;
import com.company.turbohire.backend.dto.interviewFeedback.SubmitFeedbackRequestDto;
import com.company.turbohire.backend.entity.InterviewFeedback;
import com.company.turbohire.backend.security.util.SecurityUtils;
import com.company.turbohire.backend.services.InterviewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submitFeedback(
            @PathVariable Long id,
            @RequestBody SubmitFeedbackRequestDto request
    ) {

        Long actorUserId = SecurityUtils.getCurrentUserId();

        feedbackService.submitFeedback(id, request, actorUserId);

        return ResponseEntity.ok().build();
    }

    /**
     * Get previous round feedback for an interview
     */
    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @GetMapping("/{id}/previous")
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
    @PreAuthorize("hasAnyRole('RECRUITER')")
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
                .interviewerName(feedback.getInterviewer().getFullName())
                .roundName(feedback.getInterview().getRound().getRoundName()) // ✅ ADD THIS
                .rating(feedback.getRating())
                .recommendation(feedback.getRecommendation())
                .comments(feedback.getComments())
                .submittedAt(feedback.getSubmittedAt())
                .build();
    }

}

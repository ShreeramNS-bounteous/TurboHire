package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.interview.*;
import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.security.util.SecurityUtils;
import com.company.turbohire.backend.services.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<InterviewResponseDto> createInterview(
            @RequestBody CreateInterviewRequestDto request

    ) {
        Long actorUserId = SecurityUtils.getCurrentUserId();
        Interview interview = interviewService.createInterview(request.getCandidateJobId(),
                request.getJobRoundId(), actorUserId);
        InterviewResponseDto dto = mapToDto(interview);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/{id}/assign-interviewer")
    public ResponseEntity<Void> assignInterviewer(
            @PathVariable Long id,
            @RequestBody AssignInterviewerRequestDto request
    ) {
        interviewService.assignInterviewer(id, request.getInterviewerUserId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @PostMapping("/{id}/book-slot")
    public ResponseEntity<Void> bookSlot(
            @PathVariable Long id,
            @RequestBody BookInterviewSlotRequestDto request
    ) {
        interviewService.bookInterviewSlot(id, request.getInterviewerSlotId(), request.getBookedByUserId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponseDto> getInterview(@PathVariable Long id) {
        Interview interview = interviewService.getInterview(id);
        return ResponseEntity.ok(mapToDto(interview));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<InterviewSummaryDto>> getInterviewsForJob(@PathVariable Long jobId) {
        List<InterviewSummaryDto> list = interviewService.getInterviewsForJob(jobId).stream()
                .map(this::mapToSummaryDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<InterviewSummaryDto>> getInterviewsForCandidate(@PathVariable Long candidateId) {
        List<InterviewSummaryDto> list = interviewService.getInterviewsForCandidate(candidateId).stream()
                .map(this::mapToSummaryDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelInterview(@PathVariable Long id) {
        interviewService.cancelInterview(id);
        return ResponseEntity.ok().build();
    }

    private InterviewResponseDto mapToDto(Interview i) {
        return InterviewResponseDto.builder()
                .id(i.getId())
                .candidateJobId(i.getCandidateJob().getId())
                .jobRoundId(i.getRound().getId())
                .status(i.getStatus().name())
                .build();
    }

    private InterviewSummaryDto mapToSummaryDto(Interview i) {
        return InterviewSummaryDto.builder()
                .id(i.getId())
                .candidateJobId(i.getCandidateJob().getId())
                .jobRoundId(i.getRound().getId())
                .status(i.getStatus().name())
                .build();
    }
}

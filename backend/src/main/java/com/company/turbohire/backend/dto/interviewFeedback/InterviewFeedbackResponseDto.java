package com.company.turbohire.backend.dto.interviewFeedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO returned to frontend for a feedback
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewFeedbackResponseDto {
    private Long id;
    private Long interviewId;
    private Long interviewerId;
    private String interviewerName;
    private Integer rating;
    private String recommendation;
    private String comments;
    private LocalDateTime submittedAt;
}

package com.company.turbohire.backend.dto.interviewFeedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitInterviewFeedbackRequestDto {
    private Long interviewId;
    private Long interviewerUserId;
    private Integer rating;
    private String recommendation;
    private String comments;
}

package com.company.turbohire.backend.dto.interviewFeedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// For pending feedback list (interview + assigned interviewer)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingInterviewResponseDto {
    private Long interviewId;
    private Long candidateJobId;
    private String candidateName;
    private Long interviewerId;
    private String interviewerName;
    private LocalDateTime scheduledAt; // optional if you track it
}

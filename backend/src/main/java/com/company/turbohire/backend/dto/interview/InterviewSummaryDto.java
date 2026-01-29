package com.company.turbohire.backend.dto.interview;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSummaryDto {
    private Long id;
    private Long candidateJobId;
    private Long jobRoundId;
    private String status;
}

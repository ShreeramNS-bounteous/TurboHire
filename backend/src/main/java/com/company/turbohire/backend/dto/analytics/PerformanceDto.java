package com.company.turbohire.backend.dto.analytics;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
public class PerformanceDto {

    private Long candidateId;
    private Long jobId;

    private List<RoundFeedbackDto> rounds;

    private String finalRecommendation;
}

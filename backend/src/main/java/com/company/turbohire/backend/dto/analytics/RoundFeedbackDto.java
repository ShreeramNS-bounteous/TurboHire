package com.company.turbohire.backend.dto.analytics;

import lombok.*;

@Getter
@Setter @Builder
public class RoundFeedbackDto {

    private Long interviewId;
    private Integer rating;
    private String recommendation;
}


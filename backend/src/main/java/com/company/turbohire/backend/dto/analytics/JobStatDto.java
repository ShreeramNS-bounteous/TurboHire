package com.company.turbohire.backend.dto.analytics;

import lombok.*;

@Getter @Setter @Builder
public class JobStatDto {
    private Long jobId;
    private long applied;
    private long rejected;
    private long offers;
}
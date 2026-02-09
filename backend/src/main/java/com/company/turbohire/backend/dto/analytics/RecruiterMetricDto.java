package com.company.turbohire.backend.dto.analytics;

import lombok.*;

@Getter
@Setter @Builder
public class RecruiterMetricDto {

    private Long recruiterId;
    private long actions;
}


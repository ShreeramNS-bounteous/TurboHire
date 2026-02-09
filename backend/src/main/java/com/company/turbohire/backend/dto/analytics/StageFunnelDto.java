package com.company.turbohire.backend.dto.analytics;

import lombok.*;

@Getter @Setter @Builder
public class StageFunnelDto {
    private String stage;
    private long count;
}


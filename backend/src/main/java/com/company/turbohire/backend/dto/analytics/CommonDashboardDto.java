package com.company.turbohire.backend.dto.analytics;

import lombok.*;
import java.util.List;

@Getter @Setter @Builder
public class CommonDashboardDto {

    private long totalCandidates;
    private long totalRejected;
    private long totalOffers;

    private List<StageFunnelDto> funnel;
    private List<JobStatDto> jobStats;
    private List<RecruiterMetricDto> recruiterMetrics;
}

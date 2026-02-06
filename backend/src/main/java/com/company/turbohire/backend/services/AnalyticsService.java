package com.company.turbohire.backend.services;

import com.company.turbohire.backend.dto.analytics.*;
import com.company.turbohire.backend.repository.AnalyticsAuditRepository;
import com.company.turbohire.backend.repository.AnalyticsHiringRepository;
import com.company.turbohire.backend.entity.AuditLog;
import com.company.turbohire.backend.entity.HiringEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsAuditRepository auditRepo;
    private final AnalyticsHiringRepository hiringRepo;

    // ================= COMMON DASHBOARD =================

    public CommonDashboardDto getCommonDashboard() {

        long total = hiringRepo.countByEventType("CANDIDATE_ADDED");
        long rejected = hiringRepo.countByEventType("CANDIDATE_REJECTED");
        long offers = hiringRepo.countByEventType("OFFER_ACCEPTED");

        // ---- Funnel from Audit Logs ----
        Map<String, Long> funnelMap =
                auditRepo.findStageChanges()
                        .stream()
                        .collect(Collectors.groupingBy(
                                a -> extract(a.getMetaData(), "newStage"),
                                Collectors.counting()
                        ));

        List<StageFunnelDto> funnel =
                funnelMap.entrySet().stream()
                        .map(e -> StageFunnelDto.builder()
                                .stage(e.getKey())
                                .count(e.getValue())
                                .build())
                        .toList();

        // ---- Job Stats ----
        Map<Long, List<HiringEvent>> byJob =
                hiringRepo.findAll()
                        .stream()
                        .collect(Collectors.groupingBy(
                                HiringEvent::getJobId
                        ));

        List<JobStatDto> jobStats = new ArrayList<>();

        for (var e : byJob.entrySet()) {

            long applied = e.getValue().stream()
                    .filter(v -> v.getEventType().equals("CANDIDATE_ADDED"))
                    .count();

            long rej = e.getValue().stream()
                    .filter(v -> v.getEventType().equals("CANDIDATE_REJECTED"))
                    .count();

            long off = e.getValue().stream()
                    .filter(v -> v.getEventType().equals("OFFER_ACCEPTED"))
                    .count();

            jobStats.add(JobStatDto.builder()
                    .jobId(e.getKey())
                    .applied(applied)
                    .rejected(rej)
                    .offers(off)
                    .build());
        }

        // ---- Recruiter Metrics ----
        Map<Long, Long> recruiter =
                auditRepo.findAll()
                        .stream()
                        .collect(Collectors.groupingBy(
                                AuditLog::getUserId,
                                Collectors.counting()
                        ));

        List<RecruiterMetricDto> metrics =
                recruiter.entrySet().stream()
                        .map(e -> RecruiterMetricDto.builder()
                                .recruiterId(e.getKey())
                                .actions(e.getValue())
                                .build())
                        .toList();

        return CommonDashboardDto.builder()
                .totalCandidates(total)
                .totalRejected(rejected)
                .totalOffers(offers)
                .funnel(funnel)
                .jobStats(jobStats)
                .recruiterMetrics(metrics)
                .build();
    }

    // ================= PERFORMANCE DASHBOARD =================

    public PerformanceDto getPerformance(
            Long candidateId,
            Long jobId
    ) {

        List<HiringEvent> events =
                hiringRepo.findByCandidateId(candidateId);

        List<AuditLog> feedbackLogs =
                auditRepo.findByAction("FEEDBACK_SUBMITTED");

        List<RoundFeedbackDto> rounds = feedbackLogs.stream()
                .map(a -> RoundFeedbackDto.builder()
                        .interviewId(a.getEntityId())
                        .rating(
                                Integer.valueOf(
                                        extract(a.getMetaData(), "rating")
                                )
                        )
                        .recommendation(
                                extract(a.getMetaData(), "recommendation")
                        )
                        .build())
                .toList();

        String finalRec =
                rounds.stream()
                        .map(RoundFeedbackDto::getRecommendation)
                        .reduce((a, b) -> b)
                        .orElse("PENDING");

        return PerformanceDto.builder()
                .candidateId(candidateId)
                .jobId(jobId)
                .rounds(rounds)
                .finalRecommendation(finalRec)
                .build();
    }

    // ================= UTIL =================

    private String extract(String meta, String key) {
        if (meta == null) return "";
        try {
            int i = meta.indexOf(key + "=");
            if (i == -1) return "";

            int start = i + key.length() + 1;
            int end = meta.indexOf(",", start);

            return end == -1
                    ? meta.substring(start).replace("}", "")
                    : meta.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }
}

package com.company.turbohire.backend.services;

import com.company.turbohire.backend.dto.analytics.*;
import com.company.turbohire.backend.entity.Job;
import com.company.turbohire.backend.repository.AnalyticsAuditRepository;
import com.company.turbohire.backend.repository.AnalyticsHiringRepository;
import com.company.turbohire.backend.entity.AuditLog;
import com.company.turbohire.backend.entity.HiringEvent;
import com.company.turbohire.backend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsAuditRepository auditRepo;
    private final AnalyticsHiringRepository hiringRepo;
    private final JobRepository jobRepository;

    // ============= ADMIN DASHBOARD =============
    public CommonDashboardDto getCommonDashboard() {

        List<Long> jobIds =
                jobRepository.findAll()
                        .stream().map(Job::getId).toList();

        return buildDashboardForJobs(jobIds, null, null, null, null);
    }

    // ============= HR DASHBOARD =============
    public CommonDashboardDto getDashboardForHr(Long hrId) {

        List<Long> jobIds =
                jobRepository.findByCreatedBy(hrId)
                        .stream().map(Job::getId).toList();

        return buildDashboardForJobs(jobIds, null, null, null, null);
    }

    // ============= FILTERED =============
    public CommonDashboardDto getFilteredStats(
            Long userId,
            String role,
            LocalDate from,
            LocalDate to,
            Long interviewerId,
            String round) {

        List<Long> jobIds;

        if ("ADMIN".equals(role)) {
            jobIds = jobRepository.findAll()
                    .stream().map(Job::getId).toList();
        } else {
            jobIds = jobRepository.findByCreatedBy(userId)
                    .stream().map(Job::getId).toList();
        }

        return buildDashboardForJobs(
                jobIds, from, to, interviewerId, round);
    }

    // ============= CORE BUILDER =============
    private CommonDashboardDto buildDashboardForJobs(
            List<Long> jobIds,
            LocalDate from,
            LocalDate to,
            Long interviewerId,
            String round) {

        List<HiringEvent> events =
                hiringRepo.findAll()
                        .stream()
                        .filter(e -> jobIds.contains(e.getJobId()))
                        .filter(e -> filterDate(e, from, to))
                        .toList();

        long applied = count(events, "CANDIDATE_ADDED");
        long rejected = count(events, "CANDIDATE_REJECTED");
        long offers = count(events, "OFFER_ACCEPTED");

        Map<String, Long> funnel =
                getStageFunnel(jobIds, from, to, round);

        return CommonDashboardDto.builder()
                .totalCandidates(applied)
                .totalRejected(rejected)
                .totalOffers(offers)
                .funnel(toDto(funnel))
                .build();
    }

    // ============= UTIL METHODS =============

    private boolean filterDate(
            HiringEvent e,
            LocalDate from,
            LocalDate to) {

        if (from == null && to == null) return true;

        LocalDate date = e.getEventTime().toLocalDate();

        if (from != null && date.isBefore(from))
            return false;

        if (to != null && date.isAfter(to))
            return false;

        return true;
    }

    private long count(
            List<HiringEvent> events,
            String type) {

        return events.stream()
                .filter(e -> e.getEventType().equals(type))
                .count();
    }

    private Map<String, Long> getStageFunnel(
            List<Long> jobIds,
            LocalDate from,
            LocalDate to,
            String round) {

        return auditRepo.findStageChanges()
                .stream()
                .filter(a -> jobIds.contains(a.getEntityId()))
                .collect(Collectors.groupingBy(
                        a -> extract(a.getMetaData(), "newStage"),
                        Collectors.counting()
                ));
    }

    private List<StageFunnelDto> toDto(
            Map<String, Long> map) {

        return map.entrySet()
                .stream()
                .map(e -> StageFunnelDto.builder()
                        .stage(e.getKey())
                        .count(e.getValue())
                        .build())
                .toList();
    }

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

package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.HiringEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalyticsHiringRepository extends JpaRepository<HiringEvent, Long> {

    long countByEventType(String type);

    List<HiringEvent> findByJobId(Long jobId);

    List<HiringEvent> findByCandidateId(Long candidateId);
}

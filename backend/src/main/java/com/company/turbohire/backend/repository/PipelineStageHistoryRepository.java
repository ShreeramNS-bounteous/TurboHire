package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.PipelineStageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipelineStageHistoryRepository
        extends JpaRepository<PipelineStageHistory, Long> {

    // timeline for frontend
    List<PipelineStageHistory> findByCandidateJobId(Long candidateJobId);
}

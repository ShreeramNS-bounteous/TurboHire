package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.PipelineStageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineStageHistoryRepository extends JpaRepository<PipelineStageHistory, Long> {}

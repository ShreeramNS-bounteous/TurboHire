package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AnalyticsAuditRepository extends JpaRepository<AuditLog, Long> {

    long countByAction(String action);

    List<AuditLog> findByAction(String action);

    @Query("SELECT a FROM AuditLog a WHERE a.action='STAGE_CHANGED'")
    List<AuditLog> findStageChanges();

    @Query("SELECT a FROM AuditLog a WHERE a.action='FEEDBACK_SUBMITTED' AND a.entityId=?1")
    List<AuditLog> findFeedbackForInterview(Long interviewId);

    List<AuditLog> findByUserId(Long userId);
}

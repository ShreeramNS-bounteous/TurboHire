package com.company.turbohire.backend.common;

import com.company.turbohire.backend.entity.AuditLog;
import com.company.turbohire.backend.entity.HiringEvent;
import com.company.turbohire.backend.repository.AuditLogRepository;
import com.company.turbohire.backend.repository.HiringEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SystemLogger {

    private final AuditLogRepository auditLogRepository;
    private final HiringEventRepository hiringEventRepository;

    public void audit(
            Long actorUserId,
            String action,
            String entityType,
            Long entityId
    ) {
        AuditLog log = AuditLog.builder()
                .userId(actorUserId)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .build();

        auditLogRepository.save(log);
    }

    public void audit(
            Long actorUserId,
            String action,
            String entityType,
            Long entityId,
            Map<String, Object> meta
    ) {

        AuditLog log = AuditLog.builder()
                .userId(actorUserId)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .metaData(meta != null ? meta.toString() : null)
                .build();

        auditLogRepository.save(log);
    }

    public void hiringEvent(
            Long candidateId,
            Long jobId,
            Long buId,
            String eventType
    ) {
        HiringEvent event = HiringEvent.builder()
                .candidateId(candidateId)
                .jobId(jobId)
                .buId(buId)
                .eventType(eventType)
                .build();

        hiringEventRepository.save(event);
    }
}

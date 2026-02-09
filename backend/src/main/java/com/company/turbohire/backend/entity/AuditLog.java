package com.company.turbohire.backend.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    @Column(nullable = false)
    private Long userId;   // ACTOR

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entityType;

    @Column(nullable = false)
    private Long entityId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "jsonb")
    private String metaData;
}


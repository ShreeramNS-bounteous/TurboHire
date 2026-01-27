package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pipeline_stage_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineStageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stageHistoryId;

    @Column(nullable = false)
    private Long candidateJobId;

    private String fromStage;

    @Column(nullable = false)
    private String toStage;

    @Column(nullable = false)
    private Long changedByUserId;

    @CreationTimestamp
    private LocalDateTime changedAt;

    public static PipelineStageHistory create(
            Long candidateJobId,
            String fromStage,
            String toStage,
            Long actorUserId
    ) {
        return PipelineStageHistory.builder()
                .candidateJobId(candidateJobId)
                .fromStage(fromStage)
                .toStage(toStage)
                .changedByUserId(actorUserId)
                .build();
    }
}

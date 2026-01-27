package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "candidate_job",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"candidate_id", "job_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // maps to candidate_job_id

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bu_id")
    private BusinessUnit businessUnit;

    @Column(name = "match_score")
    private Double matchScore;

    @Column(name = "current_stage")
    private String currentStage;   // SCREENING, TECH_ROUND, HR, OFFER

    @Column(nullable = false)
    private String status;   // IN_PROGRESS, REJECTED, HIRED

    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @Column(name = "offer_ctc")
    private Double offerCtc;

    @Column(name = "offer_status")
    private String offerStatus; // RELEASED, ACCEPTED, DECLINED

    @Column(name = "offer_released_at")
    private LocalDateTime offerReleasedAt;

    @Column(name = "offer_accepted_at")
    private LocalDateTime offerAcceptedAt;

    @PrePersist
    private void prePersist() {
        this.appliedAt = LocalDateTime.now();
        this.status = "IN_PROGRESS";
    }
}

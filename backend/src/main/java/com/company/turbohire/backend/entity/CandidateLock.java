package com.company.turbohire.backend.entity;

import com.company.turbohire.backend.enums.CandidateLockStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "candidate_lock",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"candidate_id", "locked_bu_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "locked_bu_id")
    private BusinessUnit lockedBusinessUnit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "locked_job_id")
    private Job lockedJob;

    @Enumerated(EnumType.STRING)
    @Column(name = "lock_status", nullable = false)
    private CandidateLockStatus lockStatus;

    @Column(name = "locked_at", nullable = false, updatable = false)
    private LocalDateTime lockedAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @PrePersist
    private void prePersist() {
        this.lockedAt = LocalDateTime.now();
        this.lockStatus = CandidateLockStatus.LOCKED;
    }
}

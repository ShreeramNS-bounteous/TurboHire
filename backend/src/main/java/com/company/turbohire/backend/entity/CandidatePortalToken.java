package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_portal_token",
        uniqueConstraints = @UniqueConstraint(columnNames = "token"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatePortalToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_job_id")
    private CandidateJob candidateJob;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private int failedAttempts;


    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
        this.expiresAt = this.createdAt.plusDays(7);
    }
}

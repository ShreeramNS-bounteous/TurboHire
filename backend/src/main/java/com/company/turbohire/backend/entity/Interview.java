package com.company.turbohire.backend.entity;

import com.company.turbohire.backend.enums.InterviewMode;
import com.company.turbohire.backend.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_job_id")
    private CandidateJob candidateJob;

    @ManyToOne(optional = false)
    @JoinColumn(name = "round_id")
    private JobRound round;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewMode mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    @PrePersist
    private void prePersist() {
        this.status = InterviewStatus.SCHEDULED;
    }
}

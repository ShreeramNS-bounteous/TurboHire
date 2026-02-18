    package com.company.turbohire.backend.entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;

    @Entity
    @Table(
            name = "interview_feedback",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"interview_id", "interviewer_user_id"})
            }
    )
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class InterviewFeedback {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;   // maps to feedback_id

        @ManyToOne(optional = false)
        @JoinColumn(name = "interview_id")
        private Interview interview;

        @ManyToOne(optional = false)
        @JoinColumn(name = "interviewer_user_id")
        private User interviewer;

        private Integer rating; // 1–5

        private String recommendation; // HIRE / REJECT / HOLD

        @Column(columnDefinition = "TEXT")
        private String comments;

        @Column(name = "submitted_at", nullable = false, updatable = false)
        private LocalDateTime submittedAt;


        @PrePersist
        private void prePersist() {
            this.submittedAt = LocalDateTime.now();
        }
    }

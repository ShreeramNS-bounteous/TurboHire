package com.company.turbohire.backend.entity;

import com.company.turbohire.backend.enums.AttendanceStatus;
import com.company.turbohire.backend.enums.DecisionStatus;
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

    @Column(name = "meeting_url")
    private String meetingUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status")
    private AttendanceStatus attendanceStatus;

    @Column(name = "feedback_submitted", nullable = false)
    private boolean feedbackSubmitted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_status")
    private DecisionStatus decisionStatus;



    @PrePersist
    private void prePersist() {
        this.decisionStatus = DecisionStatus.PENDING_DECISION;
        this.status = InterviewStatus.CREATED;
    }
}

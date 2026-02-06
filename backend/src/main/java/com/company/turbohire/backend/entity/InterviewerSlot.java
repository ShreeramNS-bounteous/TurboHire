package com.company.turbohire.backend.entity;

import com.company.turbohire.backend.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "interviewer_slot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewerSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // maps to slot_id

    @ManyToOne(optional = false)
    @JoinColumn(name = "interviewer_id")
    private InterviewerProfile interviewer;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;// AVAILABLE / BOOKED / BLOCKED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = SlotStatus.AVAILABLE; // ✅ Assign enum, not string
        }
    }

    @Column(name = "posted_by")
    private Long postedByInterviewerId;

    @Column(name = "visible_to_hr")
    private Long visibleToHrId;

}

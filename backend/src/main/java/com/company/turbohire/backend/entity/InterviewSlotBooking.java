package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "interview_slot_booking",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"interview_id", "slot_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlotBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // maps to booking_id

    @ManyToOne(optional = false)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(optional = false)
    @JoinColumn(name = "slot_id")
    private InterviewerSlot slot;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booked_by_user_id")
    private User bookedBy;

    @Column(name = "booked_at", nullable = false, updatable = false)
    private LocalDateTime bookedAt;

    @PrePersist
    private void prePersist() {
        this.bookedAt = LocalDateTime.now();
    }
}

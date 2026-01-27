package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "hiring_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HiringEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private Long candidateId;
    private Long jobId;
    private Long buId;

    @Column(nullable = false)
    private String eventType;

    @CreationTimestamp
    private LocalDateTime eventTime;
}

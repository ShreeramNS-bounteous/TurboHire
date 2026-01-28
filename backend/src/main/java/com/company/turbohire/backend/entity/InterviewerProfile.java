package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interviewer_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewerProfile {

    @Id
    private Long id;   // same as users.id

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "interviewer_id")
    private User user;

    private String expertise; // Java, System Design, React

    private String timezone;  // IST, PST, CET

    @Column(nullable = false)
    private String status;    // ACTIVE / INACTIVE



}

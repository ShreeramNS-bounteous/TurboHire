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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    // Employee details
    private String expertise;
    // skills
    @Column(name = "experience_years")
    private int experienceYears;

    // Interviewer flag
    @Column(name = "is_interviewer", nullable = false)
    private boolean isInterviewer;   // 👈 KEY CHANGE

}

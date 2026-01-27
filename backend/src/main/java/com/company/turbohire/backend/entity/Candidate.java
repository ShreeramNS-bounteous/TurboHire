package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "candidate",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String source; // LinkedIn, Naukri, Referral

    @Column(nullable = false)
    private String status; // ACTIVE, LOCKED, REJECTED, HIRED

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }
}

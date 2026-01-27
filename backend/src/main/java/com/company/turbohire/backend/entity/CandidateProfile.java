package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfile {

    @Id
    private Long id;   // same as candidate.id

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "total_experience")
    private Double totalExperience; // years

    @Column(columnDefinition = "jsonb")
    private String skills; // parsed skills JSON

    @Column(columnDefinition = "jsonb")
    private String education; // parsed education JSON

    @Column(name = "current_company")
    private String currentCompany;
}

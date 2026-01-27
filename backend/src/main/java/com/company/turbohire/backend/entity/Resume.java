package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // maps to resume_id

    @OneToOne(optional = false)
    @JoinColumn(name = "candidate_id", unique = true)
    private Candidate candidate;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType; // application/pdf

    @Lob
    @Column(name = "resume_pdf", nullable = false)
    private byte[] resumePdf;

    @Column(name = "source_portal")
    private String sourcePortal; // INTERNAL, CAMPUS, PARTNER

    @Column(name = "is_editable", nullable = false)
    private Boolean isEditable;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    private void prePersist() {
        this.uploadedAt = LocalDateTime.now();
        this.isEditable = false; // candidate cannot change resume
        this.fileType = "application/pdf";
    }
}

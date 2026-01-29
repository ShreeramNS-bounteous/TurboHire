package com.company.turbohire.backend.dto.candidate;

import lombok.Data;

@Data
public class CreateCandidateRequest {

    // Candidate
    private String fullName;
    private String email;
    private String phone;
    private String source;

    // Profile
    private Double totalExperience;
    private String skills;
    private String education;
    private String currentCompany;

    // Resume
    private String fileName;
    private byte[] resumePdf;
}

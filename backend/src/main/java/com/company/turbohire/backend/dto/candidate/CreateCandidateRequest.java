package com.company.turbohire.backend.dto.candidate;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CreateCandidateRequest {

    // Candidate
    private String fullName;
    private String email;
    private String phone;
    private String source;

    // Profile
    private Double totalExperience;
    private List<String> skills;
    private Map<String, Object> education;
    private String currentCompany;

    // Resume
    private String fileName;
    private byte[] resumePdf;
}

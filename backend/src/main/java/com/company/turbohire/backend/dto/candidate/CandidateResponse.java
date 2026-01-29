package com.company.turbohire.backend.dto.candidate;

import com.company.turbohire.backend.entity.Candidate;
import lombok.Data;

@Data
public class CandidateResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String source;
    private String status;

    public static CandidateResponse from(Candidate candidate) {
        CandidateResponse res = new CandidateResponse();
        res.setId(candidate.getId());
        res.setFullName(candidate.getFullName());
        res.setEmail(candidate.getEmail());
        res.setPhone(candidate.getPhone());
        res.setSource(candidate.getSource());
        res.setStatus(candidate.getStatus());
        return res;
    }
}

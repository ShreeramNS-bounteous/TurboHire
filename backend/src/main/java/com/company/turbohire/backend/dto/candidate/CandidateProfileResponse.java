package com.company.turbohire.backend.dto.candidate;

import com.company.turbohire.backend.entity.CandidateProfile;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CandidateProfileResponse {

    private Long candidateId;
    private Double totalExperience;
    private List<String> skills;
    private Map<String, Object> education;
    private String currentCompany;

    public static CandidateProfileResponse from(CandidateProfile p) {
        CandidateProfileResponse res = new CandidateProfileResponse();
        res.setCandidateId(p.getCandidate().getId());
        res.setTotalExperience(p.getTotalExperience());
        res.setSkills(p.getSkills());
        res.setEducation(p.getEducation());
        res.setCurrentCompany(p.getCurrentCompany());
        return res;
    }
}

package com.company.turbohire.backend.dto.candidate;

import com.company.turbohire.backend.entity.Candidate;
import com.company.turbohire.backend.entity.CandidateProfile;
import com.company.turbohire.backend.entity.Resume;
import java.util.Base64;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CandidateProfileResponse {

    // Candidate (basic)
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String source;
    private String status;

    // Profile
    private Double totalExperience;
    private List<String> skills;
    private Map<String, Object> education;
    private String currentCompany;

    // Resume
    private String resumeFileName;
    private String resumeFileType;
    private String resumePdf;

    public static CandidateProfileResponse from(
            Candidate candidate,
            CandidateProfile profile,
            Resume resume
    ) {

        CandidateProfileResponse res = new CandidateProfileResponse();

        // Candidate
        res.setId(candidate.getId());
        res.setFullName(candidate.getFullName());
        res.setEmail(candidate.getEmail());
        res.setPhone(candidate.getPhone());
        res.setSource(candidate.getSource());
        res.setStatus(candidate.getStatus());

        // Profile
        if (profile != null) {
            res.setTotalExperience(profile.getTotalExperience());
            res.setSkills(profile.getSkills());
            res.setEducation(profile.getEducation());
            res.setCurrentCompany(profile.getCurrentCompany());
        }

        // Resume
        // Resume
        if (resume != null && resume.getResumePdf() != null) {
            res.setResumeFileName(resume.getFileName());
            res.setResumeFileType(resume.getFileType());

            // ✅ Convert byte[] → Base64 String
            res.setResumePdf(
                    Base64.getEncoder().encodeToString(
                            resume.getResumePdf()
                    )
            );
        }


        return res;
    }
}

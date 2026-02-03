package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.candidate.*;
import com.company.turbohire.backend.entity.Candidate;
import com.company.turbohire.backend.entity.CandidateProfile;
import com.company.turbohire.backend.entity.Resume;
import com.company.turbohire.backend.security.util.SecurityUtils;
import com.company.turbohire.backend.services.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    /**
     * CREATE candidate
     * Roles: HR / ADMIN
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public Long createCandidate(
            @RequestBody CreateCandidateRequest req
    ) {

        Long actorUserId = SecurityUtils.getCurrentUserId();

        Candidate candidate = Candidate.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .source(req.getSource())
                .build();

        CandidateProfile profile = CandidateProfile.builder()
                .totalExperience(req.getTotalExperience())
                .skills(req.getSkills())
                .education(req.getEducation())
                .currentCompany(req.getCurrentCompany())
                .build();

        Resume resume = Resume.builder()
                .fileName(req.getFileName())
                .resumePdf(req.getResumePdf())
                .build();

        return candidateService.createCandidate(
                candidate,
                profile,
                resume,
                actorUserId
        );
    }

    /**
     * READ – Candidate list
     * Roles: HR / ADMIN
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public List<CandidateResponse> getAllCandidates() {
        return candidateService.getAllCandidates()
                .stream()
                .map(CandidateResponse::from)
                .toList();
    }

    /**
     * READ – Candidate basic details
     * Roles: HR / ADMIN
     */
    @GetMapping("/{candidateId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public CandidateResponse getCandidate(
            @PathVariable Long candidateId
    ) {
        return CandidateResponse.from(
                candidateService.getCandidate(candidateId)
        );
    }

    /**
     * READ – Candidate profile
     * Roles: HR / ADMIN
     */
    @GetMapping("/{candidateId}/profile")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public CandidateProfileResponse getCandidateProfile(
            @PathVariable Long candidateId
    ) {
        return CandidateProfileResponse.from(
                candidateService.getCandidateProfile(candidateId)
        );
    }

    /**
     * READ – Resume
     * Roles: HR / ADMIN
     */
    @GetMapping("/{candidateId}/resume")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public Resume getResume(
            @PathVariable Long candidateId
    ) {
        return candidateService.getResume(candidateId);
    }

    /**
     * UPDATE candidate
     * Roles: HR / ADMIN
     */
    @PutMapping("/{candidateId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public void updateCandidate(
            @PathVariable Long candidateId,
            @RequestBody UpdateCandidateRequest req
    ) {

        Long actorUserId = SecurityUtils.getCurrentUserId();

        Candidate candidate = candidateService.getCandidate(candidateId);
        candidate.setFullName(req.getFullName());
        candidate.setPhone(req.getPhone());
        candidate.setStatus(req.getStatus());

        candidateService.updateCandidate(candidate, actorUserId);
    }
}

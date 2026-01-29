package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.candidate.*;
import com.company.turbohire.backend.entity.Candidate;
import com.company.turbohire.backend.entity.CandidateProfile;
import com.company.turbohire.backend.entity.Resume;
import com.company.turbohire.backend.services.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    /**
     * CREATE candidate
     * Frontend: Candidate creation form
     */
    @PostMapping
    public Long createCandidate(
            @RequestBody CreateCandidateRequest req,
            @RequestParam Long actorUserId
    ) {

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
     * READ
     * Frontend: Candidate list page
     */
    @GetMapping
    public List<CandidateResponse> getAllCandidates() {
        return candidateService.getAllCandidates()
                .stream()
                .map(CandidateResponse::from)
                .toList();
    }

    /**
     * READ
     * Frontend: Candidate profile page
     */
    @GetMapping("/{candidateId}")
    public CandidateResponse getCandidate(
            @PathVariable Long candidateId
    ) {
        return CandidateResponse.from(
                candidateService.getCandidate(candidateId)
        );
    }

    @GetMapping("/{candidateId}")
    public CandidateProfile getCandidateProfile(
            @PathVariable Long candidateId
    ) {
        return candidateService.getCandidateProfile(candidateId);
    }


    /**
     * READ
     * Frontend: Resume view / download
     */
    @GetMapping("/{candidateId}/resume")
    public Resume getResume(
            @PathVariable Long candidateId
    ) {
        return candidateService.getResume(candidateId);
    }

    /**
     * UPDATE
     * Frontend: Edit candidate details
     */
    @PutMapping("/{candidateId}")
    public void updateCandidate(
            @PathVariable Long candidateId,
            @RequestBody UpdateCandidateRequest req,
            @RequestParam Long actorUserId
    ) {

        Candidate candidate = candidateService.getCandidate(candidateId);
        candidate.setFullName(req.getFullName());
        candidate.setPhone(req.getPhone());
        candidate.setStatus(req.getStatus());

        candidateService.updateCandidate(candidate, actorUserId);
    }
}

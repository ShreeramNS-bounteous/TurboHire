package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.Candidate;
import com.company.turbohire.backend.entity.CandidateProfile;
import com.company.turbohire.backend.entity.Resume;
import com.company.turbohire.backend.repository.CandidateProfileRepository;
import com.company.turbohire.backend.repository.CandidateRepository;
import com.company.turbohire.backend.repository.ResumeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final ResumeRepository resumeRepository;
    private final SystemLogger systemLogger;

    // WRITE
    public Long createCandidate(Candidate candidate, CandidateProfile profile, Resume resume, Long actorUserId) {

        candidateRepository.save(candidate);

        profile.setCandidate(candidate);
        candidateProfileRepository.save(profile);

        resume.setCandidate(candidate);
        resumeRepository.save(resume);

        systemLogger.audit(actorUserId, "CREATE_CANDIDATE", "CANDIDATE", candidate.getId());
        return candidate.getId();
    }

    // READ
    public Candidate getCandidate(Long candidateId) {
        return candidateRepository.findById(candidateId).orElseThrow();
    }

    public CandidateProfile getCandidateProfile(Long candidateId) {
        return candidateProfileRepository.findById(candidateId).orElseThrow();
    }

    public Resume getResume(Long candidateId) {
        return resumeRepository.findByCandidate_Id(candidateId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
    }


    // READ
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }



    // UPDATE
    public void updateCandidate(Candidate candidate, Long actorUserId) {
        candidateRepository.save(candidate);
        systemLogger.audit(actorUserId, "UPDATE_CANDIDATE", "CANDIDATE", candidate.getId());
    }
}

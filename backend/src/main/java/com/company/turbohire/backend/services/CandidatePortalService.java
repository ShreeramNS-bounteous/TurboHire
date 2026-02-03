package com.company.turbohire.backend.services;

import com.company.turbohire.backend.dto.candidatePortal.CandidatePortalResponse;
import com.company.turbohire.backend.dto.interview.InterviewResponseDto;
import com.company.turbohire.backend.dto.offer.OfferResponse;
import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.CandidatePortalToken;
import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.repository.CandidatePortalTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidatePortalService {

    private final CandidatePortalTokenRepository tokenRepository;
    private final InterviewService interviewService;

    public CandidatePortalResponse getPortalByToken(String token) {

        CandidatePortalToken portalToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("Invalid portal token"));

        try {

            if (portalToken.getFailedAttempts() >= 5) {
                throw new RuntimeException("Portal access blocked due to multiple attempts");
            }


            if (portalToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("This portal link has expired");
            }

            CandidateJob cj = portalToken.getCandidateJob();

            // =======================
            // INTERVIEW (optional)
            // =======================
            InterviewResponseDto interviewDto = null;

            List<Interview> interviews =
                    interviewService.getInterviewsForCandidate(
                            cj.getCandidate().getId()
                    );

            if (interviews != null && !interviews.isEmpty()) {
                Interview latest = interviews.get(interviews.size() - 1);

                interviewDto = InterviewResponseDto.builder()
                        .id(latest.getId())
                        .candidateJobId(latest.getCandidateJob().getId())
                        .jobRoundId(latest.getRound().getId())
                        .status(latest.getStatus().name())
                        .build();
            }

            // =======================
            // OFFER (optional)
            // =======================
            OfferResponse offerDto = null;
            if (cj.getOfferStatus() != null) {
                offerDto = OfferResponse.from(cj);
            }

            return CandidatePortalResponse.builder()
                    .candidateName(cj.getCandidate().getFullName())
                    .jobTitle(cj.getJob().getTitle())
                    .businessUnit(cj.getBusinessUnit().getName())
                    .stage(cj.getCurrentStage())
                    .status(cj.getStatus())
                    .interview(interviewDto)
                    .offer(offerDto)
                    .build();

        } catch (RuntimeException ex) {


            portalToken.setFailedAttempts(
                    portalToken.getFailedAttempts() + 1
            );

            throw ex;
        }
    }
}

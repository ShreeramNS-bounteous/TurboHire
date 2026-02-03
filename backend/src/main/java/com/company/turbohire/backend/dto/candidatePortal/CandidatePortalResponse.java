package com.company.turbohire.backend.dto.candidatePortal;

import com.company.turbohire.backend.dto.interview.InterviewResponseDto;
import com.company.turbohire.backend.dto.offer.OfferResponse;
import com.company.turbohire.backend.entity.CandidateJob;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CandidatePortalResponse {

    private String candidateName;
    private String jobTitle;
    private String businessUnit;
    private String stage;
    private String status;

    private InterviewResponseDto interview;   // ✅ EXISTING DTO
    private OfferResponse offer;               // ✅ EXISTING DTO
}

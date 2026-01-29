package com.company.turbohire.backend.dto.offer;

import com.company.turbohire.backend.entity.CandidateJob;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfferResponse {

    private Long candidateJobId;

    private Long candidateId;
    private Long jobId;
    private Long buId;

    private Double offerCtc;
    private String offerStatus;

    private LocalDateTime offerReleasedAt;
    private LocalDateTime offerAcceptedAt;

    public static OfferResponse from(CandidateJob cj) {
        OfferResponse res = new OfferResponse();
        res.setCandidateJobId(cj.getId());
        res.setCandidateId(cj.getCandidate().getId());
        res.setJobId(cj.getJob().getId());
        res.setBuId(cj.getBusinessUnit().getId());
        res.setOfferCtc(cj.getOfferCtc());
        res.setOfferStatus(cj.getOfferStatus());
        res.setOfferReleasedAt(cj.getOfferReleasedAt());
        res.setOfferAcceptedAt(cj.getOfferAcceptedAt());
        return res;
    }
}

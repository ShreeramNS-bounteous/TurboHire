package com.company.turbohire.backend.dto.candidateJob;


import lombok.Data;

@Data
public class AddCandidateToPipelineRequest {

    private Long candidateId;
    private Long jobId;
    private Long buId;
}

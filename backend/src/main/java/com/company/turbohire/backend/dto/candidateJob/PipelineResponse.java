package com.company.turbohire.backend.dto.candidateJob;
import lombok.Data;

@Data
public class PipelineResponse {

    private Long candidateJobId;

    private Long candidateId;
    private String candidateName;

    private Long jobId;
    private String jobTitle;

    private Long buId;
    private String businessUnitName;

    private String currentStage;
    private String status;
}

package com.company.turbohire.backend.dto.job;

import lombok.Data;

@Data
public class UpdateJobRequest {

    private String title;
    private String location;
    private Integer experienceMin;
    private Integer experienceMax;
    private String status; // OPEN / CLOSED
}

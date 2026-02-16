package com.company.turbohire.backend.dto.interviewer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewerProfileResponseDto {

    private Long id;
    private Long userId;
    private String expertise;
    private int experienceYears;
    private String department;
    private boolean isInterviewer;
}

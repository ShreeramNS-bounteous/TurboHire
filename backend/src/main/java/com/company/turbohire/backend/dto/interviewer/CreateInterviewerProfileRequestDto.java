package com.company.turbohire.backend.dto.interviewer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInterviewerProfileRequestDto {

    private Long userId;
    private String expertise;
    private int experienceYears;
    private String department;
    private boolean interviewer; // isInterviewer
}

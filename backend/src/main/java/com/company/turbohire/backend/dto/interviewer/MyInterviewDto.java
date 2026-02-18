package com.company.turbohire.backend.dto.interviewer;

import com.company.turbohire.backend.enums.AttendanceStatus;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class MyInterviewDto {

    private Long interviewId;

    private Long candidateId;
    private String candidateName;
    private String candidateEmail;

    private String jobTitle;
    private Long jobId;
    private String roundName;

    private String slotDate;
    private String startTime;
    private String endTime;

    private String meetingUrl;
    private String status;
    private AttendanceStatus attendanceStatus;
    private boolean feedbackSubmitted;

    // 🔥 Proper Types (No String)
    private Map<String, Object> education;
    private List<String> skills;
    private String currentCompany;
    private Double totalExperience;
}

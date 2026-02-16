package com.company.turbohire.backend.dto.interview;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledInterviewDto {

    private Long interviewId;

    private String candidateName;
    private String candidateEmail;

    private String jobTitle;
    private String roundName;

    private String interviewerName;

    private String meetingUrl;


    // 🔥 NEW FIELDS
    private String slotDate;
    private String startTime;
    private String endTime;

}


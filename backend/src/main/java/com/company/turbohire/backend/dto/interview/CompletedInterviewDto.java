package com.company.turbohire.backend.dto.interview;

import com.company.turbohire.backend.enums.AttendanceStatus;
import com.company.turbohire.backend.enums.DecisionStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompletedInterviewDto {

    private Long interviewId;

    private String candidateName;
    private String candidateEmail;

    private String jobTitle;
    private String roundName;

    private String interviewerName;

    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private AttendanceStatus attendanceStatus;

    private boolean feedbackSubmitted;

    private Integer rating;
    private String recommendation;
    private boolean hasNextRound;
    private DecisionStatus decisionStatus;

}

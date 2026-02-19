package com.company.turbohire.backend.dto.interviewer;

import com.company.turbohire.backend.enums.AttendanceStatus;
import lombok.*;

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
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
<<<<<<< HEAD
=======
    private String evaluationTemplateCode;
    private boolean hasNextRound;
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)

    private String slotDate;
    private String startTime;
    private String endTime;

    private String meetingUrl;
    private String status;
<<<<<<< HEAD
    private AttendanceStatus attendanceStatus;
    private boolean feedbackSubmitted;

=======

    private AttendanceStatus attendanceStatus;
    private boolean feedbackSubmitted;

    private Integer rating;
    private String recommendation;
    private String comments;
    private LocalDateTime submittedAt;

>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
    // 🔥 Proper Types (No String)
    private Map<String, Object> education;
    private List<String> skills;
    private String currentCompany;
    private Double totalExperience;
}

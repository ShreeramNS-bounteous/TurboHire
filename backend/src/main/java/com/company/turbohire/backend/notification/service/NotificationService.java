package com.company.turbohire.backend.notification.service;

import com.company.turbohire.backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    // ============= 1. HR CREDENTIALS =============
    public void sendHrCredentials(User hr, String rawPassword) {

        String body =
                "<h3>Welcome to TurboHire</h3>" +
                        "Email: " + hr.getEmail() +
                        "<br>Password: " + rawPassword;

        emailService.sendMail(
                hr.getEmail(),
                "HR Account Created",
                body
        );
    }

    // ============= 2. INTERVIEWER ASSIGNMENT =============
    public void notifyInterviewer(
            User interviewer,
            CandidateJob cj,
            Interview interview
    ) {

        StringBuilder body = new StringBuilder();

        body.append("<h3>Interview Assignment</h3>");

        body.append("Candidate: ")
                .append(cj.getCandidate().getFullName())
                .append("<br>");

        body.append("Job: ")
                .append(cj.getJob().getTitle())
                .append("<br>");

        body.append("Round: ")
                .append(interview.getRound().getRoundName())
                .append("<br>");

        if (interview.getScheduledAt() != null) {
            body.append("Scheduled At: ")
                    .append(interview.getScheduledAt())
                    .append("<br>");
        } else {
            body.append("Interview not yet scheduled<br>");
        }

        body.append("<br>Please login to TurboHire to view details.");

        emailService.sendMail(
                interviewer.getEmail(),
                "Interview Assigned",
                body.toString()
        );
    }


    // ============= 3. CANDIDATE STATUS =============
    public void notifyCandidateStatus(
            Candidate candidate,
            String portalLink,
            String status
    ) {

        String body =
                "<h3>Application Status Updated</h3>" +
                        "Current Stage: " + status +
                        "<br>Portal Link (SAME):<br>" +
                        portalLink;

        emailService.sendMail(
                candidate.getEmail(),
                "Application Update",
                body
        );
    }

    // ============= 4. REMINDER =============
    public void sendReminder(
            User interviewer,
            Interview interview
    ) {

        CandidateJob cj = interview.getCandidateJob();

        StringBuilder body = new StringBuilder();

        body.append("<h3>Feedback Pending – ")
                .append(cj.getJob().getTitle())
                .append("</h3>");

        body.append("Candidate: ")
                .append(cj.getCandidate().getFullName())
                .append("<br>");

        body.append("Job: ")
                .append(cj.getJob().getTitle())
                .append("<br>");

        body.append("Round: ")
                .append(interview.getRound().getRoundName())
                .append("<br>");

        if (interview.getScheduledAt() != null) {
            body.append("Scheduled At: ")
                    .append(interview.getScheduledAt())
                    .append("<br>");
        }

        body.append("<br>Please login to TurboHire and submit feedback.");

        emailService.sendMail(
                interviewer.getEmail(),
                "Feedback Pending – " + cj.getJob().getTitle(),
                body.toString()
        );
    }

}

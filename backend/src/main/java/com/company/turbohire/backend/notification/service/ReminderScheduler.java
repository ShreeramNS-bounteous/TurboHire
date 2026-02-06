package com.company.turbohire.backend.notification.service;

import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.InterviewAssignment;
import com.company.turbohire.backend.repository.InterviewAssignmentRepository;
import com.company.turbohire.backend.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderScheduler {

    private final InterviewRepository interviewRepository;
    private final InterviewAssignmentRepository interviewAssignmentRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendReminders() {

        List<Interview> pending =
                interviewRepository.findPendingFeedback();

        for (Interview interview : pending) {

            // 🔥 YOUR REPO RETURNS LIST
            List<InterviewAssignment> assignments =
                    interviewAssignmentRepository
                            .findByInterview(interview);

            for (InterviewAssignment assignment : assignments) {

                notificationService.sendReminder(
                        assignment.getInterviewer(),
                        interview
                );
            }
        }
    }
}

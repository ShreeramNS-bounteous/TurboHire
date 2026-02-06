package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.*;
import com.company.turbohire.backend.enums.InterviewStatus;
import com.company.turbohire.backend.notification.service.NotificationService;
import com.company.turbohire.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final CandidateJobRepository candidateJobRepository;
    private final JobRoundRepository jobRoundRepository;
    private final UserRepository userRepository;
    private final InterviewAssignmentRepository interviewAssignmentRepository;
    private final InterviewerProfileRepository interviewerProfileRepository;
    private final InterviewerSlotRepository interviewerSlotRepository;
    private final InterviewSlotBookingRepository interviewSlotBookingRepository;
    private final SystemLogger systemLogger;
    private final NotificationService notificationService;
    private final CandidatePortalTokenRepository tokenRepository;


    // Create interview
    public Interview createInterview(Long candidateJobId, Long jobRoundId, Long actorUserId) {
        CandidateJob candidateJob = candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));
        JobRound jobRound = jobRoundRepository.findById(jobRoundId)
                .orElseThrow(() -> new RuntimeException("JobRound not found"));

        if (interviewRepository.existsByCandidateJobAndRound(candidateJob, jobRound)) {
            throw new RuntimeException("Interview already exists for this candidate and round");
        }

        Interview interview = Interview.builder()
                .candidateJob(candidateJob)
                .round(jobRound)
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview saved = interviewRepository.save(interview);
        systemLogger.audit(actorUserId, "CREATE_INTERVIEW", "INTERVIEW", saved.getId());

        return saved;
    }

    // Assign interviewer
    public void assignInterviewer(Long interviewId, Long interviewerUserId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        User interviewer = userRepository.findById(interviewerUserId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));
        InterviewerProfile profile = interviewerProfileRepository.findById(interviewerUserId)
                .orElseThrow(() -> new RuntimeException("Interviewer profile not found"));

        if (!"ACTIVE".equals(profile.getStatus())) {
            throw new RuntimeException("Interviewer is not active");
        }

        if (interviewAssignmentRepository.existsByInterviewAndInterviewer(interview, interviewer)) {
            throw new RuntimeException("Interviewer already assigned to this interview");
        }

        InterviewAssignment assignment = InterviewAssignment.builder()
                .interview(interview)
                .interviewer(interviewer)
                .build();
        interviewAssignmentRepository.save(assignment);

        String token =
                tokenRepository.findByCandidateJob(interview.getCandidateJob())
                        .orElseThrow(() -> new RuntimeException("Portal token not found"))
                        .getToken();

        String portalLink =
                "http://localhost:8080/api/candidate-portal?token=" + token;


        // 🔥 MAIL TO INTERVIEWER – DETAILS ONLY
        notificationService.notifyInterviewer(
                interviewer,
                interview.getCandidateJob(),
                interview
        );

        // 🔥 MAIL TO CANDIDATE (UNCHANGED)
        notificationService.notifyCandidateStatus(
                interview.getCandidateJob().getCandidate(),
                portalLink,
                interview.getCandidateJob().getCurrentStage()
        );


        // 🔥 MAIL TO CANDIDATE
        notificationService.notifyCandidateStatus(
                interview.getCandidateJob().getCandidate(),
                portalLink,
                interview.getCandidateJob().getCurrentStage()
        );

    }

    // Book slot
    public void bookInterviewSlot(Long interviewId, Long slotId, Long bookedByUserId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        InterviewerSlot slot = interviewerSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Interviewer slot not found"));

        if (slot.getStatus() != com.company.turbohire.backend.enums.SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot not available");
        }

        User bookedBy = userRepository.findById(bookedByUserId)
                .orElseThrow(() -> new RuntimeException("Booking user not found"));

        InterviewSlotBooking booking = InterviewSlotBooking.builder()
                .interview(interview)
                .slot(slot)
                .bookedBy(bookedBy)
                .build();
        interviewSlotBookingRepository.save(booking);

        slot.setStatus(com.company.turbohire.backend.enums.SlotStatus.BOOKED);
        interviewerSlotRepository.save(slot);
        interview.setStatus(InterviewStatus.SCHEDULED);
    }

    // Get interview by id
    @Transactional(readOnly = true)
    public Interview getInterview(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
    }

    // Get interviews for job
    @Transactional(readOnly = true)
    public List<Interview> getInterviewsForJob(Long jobId) {
        return interviewRepository.findByCandidateJob_Job_Id(jobId);
    }

    // Get interviews for candidate
    @Transactional(readOnly = true)
    public List<Interview> getInterviewsForCandidate(Long candidateId) {
        return interviewRepository.findByCandidateJob_Candidate_Id(candidateId);
    }

    // Cancel interview
    public void cancelInterview(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setStatus(InterviewStatus.CANCELLED);
        interviewRepository.save(interview);
    }
}

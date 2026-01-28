package com.company.turbohire.backend.services;

import com.company.turbohire.backend.entity.*;
import com.company.turbohire.backend.enums.InterviewStatus;
import com.company.turbohire.backend.enums.SlotStatus;
import com.company.turbohire.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewAssignmentRepository interviewAssignmentRepository;
    private final InterviewSlotBookingRepository interviewSlotBookingRepository;

    private final CandidateJobRepository candidateJobRepository;
    private final JobRoundRepository jobRoundRepository;
    private final UserRepository userRepository;
    private final InterviewerSlotRepository interviewerSlotRepository;
    private final InterviewerProfileRepository interviewerProfileRepository;

    /**
     * Create interview for a candidate and job round
     * One interview per candidate per round
     */
    public Interview createInterview(Long candidateJobId, Long jobRoundId) {

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
                .status(InterviewStatus.SCHEDULED) // ✅ improvement
                .build();

        return interviewRepository.save(interview);
    }

    /**
     * Assign interviewer to an interview
     */
    public void assignInterviewer(Long interviewId, Long interviewerUserId) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        User interviewer = userRepository.findById(interviewerUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
    }

    /**
     * Book interview slot
     */
    public void bookInterviewSlot(Long interviewId, Long interviewerSlotId, Long bookedByUserId) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        InterviewerSlot slot = interviewerSlotRepository.findById(interviewerSlotId)
                .orElseThrow(() -> new RuntimeException("Interviewer slot not found"));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Interviewer slot is not available");
        }

        User interviewerUser = slot.getInterviewer().getUser();

        boolean interviewerAssigned =
                interviewAssignmentRepository.existsByInterviewAndInterviewer(interview, interviewerUser);

        if (!interviewerAssigned) {
            throw new RuntimeException("Slot interviewer is not assigned to this interview");
        }

        User bookedBy = userRepository.findById(bookedByUserId)
                .orElseThrow(() -> new RuntimeException("Booking user not found"));

        InterviewSlotBooking booking = InterviewSlotBooking.builder()
                .interview(interview)
                .slot(slot)
                .bookedBy(bookedBy)
                .build();

        interviewSlotBookingRepository.save(booking);

        // update slot status
        slot.setStatus(SlotStatus.BOOKED);
        interviewerSlotRepository.save(slot);

        // update interview status
        interview.setStatus(InterviewStatus.SCHEDULED);
    }

    /**
     * Get interview details (frontend read API)
     */
    @Transactional(readOnly = true)
    public Interview getInterviewDetails(Long interviewId) {

        return interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
    }
}
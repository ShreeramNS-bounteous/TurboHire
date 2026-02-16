package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.dto.interview.CompletedInterviewDto;
import com.company.turbohire.backend.dto.interview.InterviewSummaryDto;
import com.company.turbohire.backend.dto.interview.PendingInterviewDto;
import com.company.turbohire.backend.dto.interview.ScheduledInterviewDto;
import com.company.turbohire.backend.entity.*;
import com.company.turbohire.backend.enums.*;
import com.company.turbohire.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class InterviewService {

    private final PipelineStageHistoryRepository pipelineStageHistoryRepository;
    private final InterviewRepository interviewRepository;
    private final CandidateJobRepository candidateJobRepository;
    private final JobRoundRepository jobRoundRepository;
    private final UserRepository userRepository;
    private final InterviewAssignmentRepository interviewAssignmentRepository;
    private final InterviewerProfileRepository interviewerProfileRepository;
    private final InterviewerSlotRepository interviewerSlotRepository;
    private final InterviewSlotBookingRepository interviewSlotBookingRepository;
    private final SystemLogger systemLogger;
    private final InterviewFeedbackRepository feedbackRepository;

    // Create interview
    @Transactional
    public Interview createInterview(Long candidateJobId, Long actorUserId) {

        CandidateJob candidateJob = candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        Job job = candidateJob.getJob();

        // 🔥 Automatically pick FIRST round
        JobRound firstRound = jobRoundRepository
                .findFirstByJob_IdOrderByRoundOrderAsc(job.getId())
                .orElseThrow(() -> new RuntimeException("No rounds defined for this job"));

        Interview interview = Interview.builder()
                .candidateJob(candidateJob)
                .round(firstRound)
                .status(InterviewStatus.CREATED)  // 🔥 This means "To Be Scheduled"
                .mode(InterviewMode.ONLINE)
                .build();

        interviewRepository.save(interview);

        systemLogger.audit(
                actorUserId,
                "CREATE_INTERVIEW",
                "INTERVIEW",
                interview.getId()
        );

        return interview;
    }


    // Assign interviewer
    public void assignInterviewer(Long interviewId, Long interviewerUserId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        User interviewer = userRepository.findById(interviewerUserId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));
        InterviewerProfile profile = interviewerProfileRepository.findById(interviewerUserId)
                .orElseThrow(() -> new RuntimeException("Interviewer profile not found"));
        if (!profile.isInterviewer()) {
            throw new RuntimeException("User is not marked as interviewer");
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

    // Book slot
    @Transactional
    public void bookInterviewSlot(Long interviewId, Long slotId, Long bookedByUserId, String meetingLink) {

        // 1️⃣ Fetch interview
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        // 2️⃣ Fetch slot
        InterviewerSlot slot = interviewerSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Interviewer slot not found"));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot not available");
        }

        // 3️⃣ Fetch recruiter (who booked)
        User bookedBy = userRepository.findById(bookedByUserId)
                .orElseThrow(() -> new RuntimeException("Booking user not found"));

        // 🔥 IMPORTANT: interviewer is slot owner
        Long interviewerUserId = slot.getUserId();

        User interviewer = userRepository.findById(interviewerUserId)
                .orElseThrow(() -> new RuntimeException("Interviewer user not found"));

        // 4️⃣ Save InterviewSlotBooking (keep your logic)
        InterviewSlotBooking booking = InterviewSlotBooking.builder()
                .interview(interview)
                .slot(slot)
                .bookedBy(bookedBy)
                .build();

        interviewSlotBookingRepository.save(booking);

        // 5️⃣ Create InterviewAssignment (🔥 NEW PART)
        InterviewAssignment assignment = InterviewAssignment.builder()
                .id(new InterviewAssignmentId(interview.getId(), interviewerUserId))
                .interview(interview)
                .interviewer(interviewer)
                .build();

        interviewAssignmentRepository.save(assignment);

        // 6️⃣ Mark slot as BOOKED
        slot.setStatus(SlotStatus.BOOKED);
        interviewerSlotRepository.save(slot);

        LocalDateTime interviewStart = LocalDateTime.of(
                slot.getSlotDate(),
                slot.getStartTime()
        );

        // 7️⃣ Mark interview as SCHEDULED
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setScheduledAt(interviewStart);
        interview.setMeetingUrl(meetingLink);
        interviewRepository.save(interview);

        // 8️⃣ Mark profile as interviewer
        InterviewerProfile profile = interviewerProfileRepository
                .findByUserId(interviewerUserId)
                .orElseGet(() -> {
                    InterviewerProfile p = new InterviewerProfile();
                    p.setUserId(interviewerUserId);
                    return p;
                });

        profile.setInterviewer(true);
        interviewerProfileRepository.save(profile);

        // 9️⃣ Move Candidate pipeline stage to interview round
        CandidateJob candidateJob = interview.getCandidateJob();

        String roundName = interview.getRound().getRoundName();

        String previousStage = candidateJob.getCurrentStage();

        candidateJob.setCurrentStage(roundName);
        candidateJobRepository.save(candidateJob);

// Move stage
        candidateJob.setCurrentStage(roundName);
        candidateJobRepository.save(candidateJob);

// Save stage history
        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        candidateJob.getId(),
                        previousStage,
                        roundName,
                        bookedByUserId
                )
        );

// 🔥 Hiring event
        systemLogger.hiringEvent(
                candidateJob.getCandidate().getId(),
                candidateJob.getJob().getId(),
                candidateJob.getBusinessUnit().getId(),
                "INTERVIEW_SCHEDULED"
        );


    }

    @Transactional(readOnly = true)
    public List<PendingInterviewDto> getPendingInterviews() {

        // 1️⃣ Get shortlisted candidates
        List<CandidateJob> activeCandidates =
                candidateJobRepository.findByStatus(
                        "IN_PROGRESS"
                );
        return activeCandidates.stream()

                .filter(cj->
                        cj.getJob() != null &&
                        !"DELETED".equalsIgnoreCase(cj.getJob().getStatus())
                        )

                .filter(cj ->
                        !interviewRepository.existsByCandidateJob_IdAndRound_RoundNameAndStatusIn(
                                cj.getId(),
                                cj.getCurrentStage(), // 🔥 THIS IS THE KEY FIX
                                List.of(
                                        InterviewStatus.SCHEDULED,
                                        InterviewStatus.COMPLETED
                                )
                        )
                )
                .map(cj -> PendingInterviewDto.builder()
                        .candidateJobId(cj.getId())
                        .candidateId(cj.getCandidate().getId())
                        .candidateName(cj.getCandidate().getFullName())
                        .candidateEmail(cj.getCandidate().getEmail())
                        .jobId(cj.getJob().getId())
                        .jobTitle(cj.getJob().getTitle())
                        .currentStage(cj.getCurrentStage())
                        .build()
                )
                .toList();
    }



    @Transactional(readOnly = true)
    public List<ScheduledInterviewDto> getScheduledInterviews() {

        List<Interview> interviews =
                interviewRepository.findByStatus(InterviewStatus.SCHEDULED);

        return interviews.stream().map(interview -> {

            // 🔥 Get assignment
            InterviewAssignment assignment =
                    interviewAssignmentRepository
                            .findByInterviewId(interview.getId())
                            .orElse(null);

            String interviewerName = null;

            if (assignment != null) {
                interviewerName =
                        assignment.getInterviewer().getFullName();
            }

            // 🔥 Get slot booking
            InterviewSlotBooking booking =
                    interviewSlotBookingRepository
                            .findByInterviewId(interview.getId())
                            .orElse(null);

            String slotDate = null;
            String startTime = null;
            String endTime = null;

            if (booking != null) {
                InterviewerSlot slot = booking.getSlot();

                slotDate = slot.getSlotDate().toString();
                startTime = slot.getStartTime().toString();
                endTime = slot.getEndTime().toString();
            }

            return ScheduledInterviewDto.builder()
                    .interviewId(interview.getId())
                    .candidateName(interview.getCandidateJob()
                            .getCandidate().getFullName())
                    .candidateEmail(interview.getCandidateJob()
                            .getCandidate().getEmail())
                    .jobTitle(interview.getCandidateJob()
                            .getJob().getTitle())
                    .roundName(interview.getRound().getRoundName())
                    .interviewerName(interviewerName)
                    .meetingUrl(interview.getMeetingUrl())
                    .slotDate(slotDate)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();

        }).toList();
    }


    @Transactional
    public void markAttendance(
            Long interviewId,
            AttendanceStatus attendanceStatus
    ) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        interview.setAttendanceStatus(attendanceStatus);

        // Move to COMPLETED
        interview.setStatus(InterviewStatus.COMPLETED);

        interviewRepository.save(interview);
    }



    @Transactional(readOnly = true)
    public List<CompletedInterviewDto> getCompletedInterviews() {

        List<Interview> interviews =
                interviewRepository.findByStatus(InterviewStatus.COMPLETED);

        return interviews.stream().map(interview -> {

            // 🔹 1. Fetch Feedback (if exists)
            InterviewFeedback feedback =
                    feedbackRepository.findByInterview_Id(interview.getId())
                            .orElse(null);

            // 🔹 2. Fetch Slot Booking
            InterviewSlotBooking booking =
                    interviewSlotBookingRepository
                            .findByInterviewId(interview.getId())
                            .orElse(null);

            LocalDate slotDate = null;
            LocalTime startTime = null;
            LocalTime endTime = null;

            if (booking != null && booking.getSlot() != null) {
                slotDate = booking.getSlot().getSlotDate();
                startTime = booking.getSlot().getStartTime();
                endTime = booking.getSlot().getEndTime();
            }

            // 🔹 3. Fetch Interviewer from Assignment table
            String interviewerName =
                    interviewAssignmentRepository
                            .findByInterviewId(interview.getId())
                            .stream()
                            .findFirst()
                            .map(a -> a.getInterviewer().getFullName())
                            .orElse("N/A");

            JobRound currentRound = interview.getRound();

            int nextOrder = currentRound.getRoundOrder() + 1;

            boolean hasNextRound =
                    jobRoundRepository.findByJob_IdAndRoundOrder(
                            currentRound.getJob().getId(),
                            nextOrder
                    ).isPresent();

            return CompletedInterviewDto.builder()
                    .interviewId(interview.getId())
                    .candidateName(
                            interview.getCandidateJob()
                                    .getCandidate()
                                    .getFullName()
                    )
                    .candidateEmail(
                            interview.getCandidateJob()
                                    .getCandidate()
                                    .getEmail()
                    )
                    .jobTitle(
                            interview.getCandidateJob()
                                    .getJob()
                                    .getTitle()
                    )
                    .roundName(
                            interview.getRound()
                                    .getRoundName()
                    )
                    .interviewerName(interviewerName)
                    .slotDate(slotDate)
                    .startTime(startTime)
                    .endTime(endTime)
                    .attendanceStatus(interview.getAttendanceStatus())
                    .feedbackSubmitted(interview.isFeedbackSubmitted())
                    .rating(feedback != null ? feedback.getRating() : null)
                    .recommendation(
                            feedback != null ? feedback.getRecommendation() : null
                    )
                    .hasNextRound(hasNextRound)
                    .decisionStatus(interview.getDecisionStatus())
                    .build();

        }).toList();
    }

    @Transactional
    public void moveToNextRound(Long interviewId, Long actorUserId) {

        // 1️⃣ Fetch interview
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        // 2️⃣ Validate interview completed
        if (interview.getStatus() != InterviewStatus.COMPLETED) {
            throw new RuntimeException("Interview not completed yet");
        }

        // 3️⃣ Validate feedback submitted
        if (!interview.isFeedbackSubmitted()) {
            throw new RuntimeException("Feedback not submitted yet");
        }

        CandidateJob candidateJob = interview.getCandidateJob();
        JobRound currentRound = interview.getRound();

        // 4️⃣ Find next round
        int nextOrder = currentRound.getRoundOrder() + 1;

        Optional<JobRound> nextRoundOpt =
                jobRoundRepository.findByJob_IdAndRoundOrder(
                        currentRound.getJob().getId(),
                        nextOrder
                );

        if (nextRoundOpt.isEmpty()) {
            throw new RuntimeException(
                    "No next round available. Candidate eligible for Hire."
            );
        }

        JobRound nextRound = nextRoundOpt.get();

        // 5️⃣ Update candidate stage
        String previousStage = candidateJob.getCurrentStage();

        candidateJob.setCurrentStage(nextRound.getRoundName());
        candidateJobRepository.save(candidateJob);

        interview.setDecisionStatus(DecisionStatus.MOVED);
        interviewRepository.save(interview);


        // 6️⃣ Save pipeline history
        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        candidateJob.getId(),
                        previousStage,
                        nextRound.getRoundName(),
                        actorUserId
                )
        );

        // 7️⃣ Log hiring event
        systemLogger.hiringEvent(
                candidateJob.getCandidate().getId(),
                candidateJob.getJob().getId(),
                candidateJob.getBusinessUnit().getId(),
                "MOVE_TO_NEXT_ROUND"
        );

        // ✅ 8️⃣ IMPORTANT: DO NOT create interview here
        // Interview for next round will be created only when recruiter books slot
    }

    @Transactional
    public void hireCandidate(Long interviewId, Long actorUserId) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        if (interview.getStatus() != InterviewStatus.COMPLETED)
            throw new RuntimeException("Interview not completed");

        if (!interview.isFeedbackSubmitted())
            throw new RuntimeException("Feedback not submitted");

        CandidateJob cj = interview.getCandidateJob();

        cj.setStatus("HIRED");
        cj.setCurrentStage("HIRED");
        candidateJobRepository.save(cj);

        interview.setDecisionStatus(DecisionStatus.HIRED);
        interviewRepository.save(interview);

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        cj.getId(),
                        cj.getCurrentStage(),
                        "HIRED",
                        actorUserId
                )
        );
    }

    @Transactional
    public void rejectCandidate(Long interviewId, Long actorUserId) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        CandidateJob cj = interview.getCandidateJob();

        cj.setStatus("REJECTED");
        cj.setCurrentStage("REJECTED");
        candidateJobRepository.save(cj);

        interview.setDecisionStatus(DecisionStatus.REJECTED);
        interviewRepository.save(interview);

        pipelineStageHistoryRepository.save(
                PipelineStageHistory.create(
                        cj.getId(),
                        cj.getCurrentStage(),
                        "REJECTED",
                        actorUserId
                )
        );
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

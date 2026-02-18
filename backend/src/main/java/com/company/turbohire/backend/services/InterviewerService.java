package com.company.turbohire.backend.services;

import com.company.turbohire.backend.dto.HR.HrInterviewerAvailabilityDto;
import com.company.turbohire.backend.dto.HR.HrSlotDto;
import com.company.turbohire.backend.dto.interviewer.InterviewerNavbarDto;
import com.company.turbohire.backend.dto.interviewer.MyInterviewDto;
import com.company.turbohire.backend.entity.*;
import com.company.turbohire.backend.enums.SlotStatus;
import com.company.turbohire.backend.repository.*;
import com.company.turbohire.backend.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewerService {

    private final InterviewerProfileRepository profileRepository;
    private final InterviewerSlotRepository slotRepository;
    private final UserRepository userRepository;
    private final InterviewAssignmentRepository interviewAssignmentRepository;
    private final InterviewSlotBookingRepository interviewSlotBookingRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    // -------- PROFILE --------

    public InterviewerProfile createOrUpdateProfile(
            Long userId,
            String expertise,
            int experienceYears,
            String department,
            boolean isInterviewer
    ) {
        InterviewerProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    InterviewerProfile p = new InterviewerProfile();
                    p.setUserId(userId);
                    return p;
                });

        profile.setExpertise(expertise);
        profile.setExperienceYears(experienceYears);
        profile.setInterviewer(isInterviewer);

        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<MyInterviewDto> getMyInterviews() {

        Long interviewerId = SecurityUtils.getCurrentUserId();

        List<InterviewAssignment> assignments =
                interviewAssignmentRepository
                        .findByInterviewer_Id(interviewerId);

        return assignments.stream()
                .map(InterviewAssignment::getInterview)
                .map(this::mapToDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public InterviewerProfile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Transactional(readOnly = true)
    public InterviewerNavbarDto getNavbarDetails() {

        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InterviewerProfile profile = profileRepository
                .findByUserId(userId)
                .orElse(null);

        return InterviewerNavbarDto.builder()
                .userId(userId)
                .fullName(user.getFullName())
                .email(user.getEmail())
                .expertise(profile != null ? profile.getExpertise() : null)
                .experienceYears(profile != null ? profile.getExperienceYears() : 0)
                .build();
    }


    // -------- AVAILABILITY --------

    public InterviewerSlot addSlot(
            Long userId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        InterviewerSlot slot = InterviewerSlot.builder()
                .userId(userId)
                .slotDate(slotDate)
                .startTime(startTime)
                .endTime(endTime)
                .status(SlotStatus.AVAILABLE)
                .build();

        return slotRepository.save(slot);
    }

    @Transactional(readOnly = true)
    public List<InterviewerSlot> getAllSlots(Long userId) {
        return slotRepository.findByUserId(userId);
    }



    @Transactional(readOnly = true)
    public List<InterviewerSlot> getAvailableSlots(Long userId) {
        return slotRepository.findByUserIdAndStatus(userId, SlotStatus.AVAILABLE);
    }

    public void deleteSlot(Long slotId) {
        InterviewerSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() == SlotStatus.BOOKED) {
            throw new RuntimeException("Cannot delete a booked slot");
        }

        slotRepository.delete(slot);
    }



    @Transactional(readOnly = true)
    public List<HrInterviewerAvailabilityDto> getAvailableInterviewersForHr(
            LocalDate date,
            LocalTime from,
            LocalTime to
    ) {

        List<InterviewerSlot> slots =
                slotRepository.findBySlotDateAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        date,
                        SlotStatus.AVAILABLE,
                        to,
                        from
                );

        if (slots.isEmpty()) {
            return List.of();
        }

        Map<Long, List<InterviewerSlot>> slotsByUser =
                slots.stream().collect(Collectors.groupingBy(InterviewerSlot::getUserId));

        List<Long> userIds = new ArrayList<>(slotsByUser.keySet());

        Map<Long, User> userMap =
                userRepository.findByIdIn(userIds)
                        .stream()
                        .collect(Collectors.toMap(User::getId, u -> u));


        Map<Long, InterviewerProfile> profileMap =
                profileRepository.findByUserIdIn(userIds)
                        .stream()
                        .collect(Collectors.toMap(InterviewerProfile::getUserId, p -> p));

        return slotsByUser.entrySet().stream().map(entry -> {
            Long userId = entry.getKey();
            InterviewerProfile profile = profileMap.get(userId);
            User user = userMap.get(userId);

            return HrInterviewerAvailabilityDto.builder()
                    .userId(userId)
                    .userName(user != null ? user.getFullName() : "Employee")
                    .expertise(profile != null ? profile.getExpertise() : null)
                    .experienceYears(
                            profile != null ? profile.getExperienceYears() : null
                    )
                    .slots(
                            entry.getValue().stream()
                                    .map(slot -> HrSlotDto.builder()
                                            .slotId(slot.getId())
                                            .startTime(slot.getStartTime())
                                            .endTime(slot.getEndTime())
                                            .build())
                                    .toList()
                    )
                    .build();
        }).toList();

    }

    private MyInterviewDto mapToDto(Interview interview) {

        InterviewSlotBooking booking =
                interviewSlotBookingRepository.findByInterviewId(interview.getId())
                        .orElse(null);

        String slotDate = null;
        String startTime = null;
        String endTime = null;

        if (booking != null && booking.getSlot() != null) {
            slotDate = booking.getSlot().getSlotDate().toString();
            startTime = booking.getSlot().getStartTime().toString();
            endTime = booking.getSlot().getEndTime().toString();
        }

        Candidate candidate = interview.getCandidateJob().getCandidate();

        CandidateProfile profile =
                candidateProfileRepository
                        .findByCandidateId(candidate.getId())
                        .orElse(null);

        return MyInterviewDto.builder()
                .interviewId(interview.getId())
                .candidateId(candidate.getId())
                .candidateName(candidate.getFullName())
                .candidateEmail(candidate.getEmail())
                .jobTitle(interview.getCandidateJob().getJob().getTitle())
                .jobId(interview.getCandidateJob().getJob().getId())
                .roundName(interview.getRound().getRoundName())
                .slotDate(slotDate)
                .startTime(startTime)
                .endTime(endTime)
                .meetingUrl(interview.getMeetingUrl())
                .status(interview.getStatus().name())
                .attendanceStatus(interview.getAttendanceStatus())
                .feedbackSubmitted(interview.isFeedbackSubmitted())

                // 🔥 PROFILE DATA
                .education(profile != null ? profile.getEducation() : null)
                .skills(profile != null ? profile.getSkills() : null)
                .currentCompany(profile != null ? profile.getCurrentCompany() : null)
                .totalExperience(profile != null ? profile.getTotalExperience() : null)

                .build();
    }



}

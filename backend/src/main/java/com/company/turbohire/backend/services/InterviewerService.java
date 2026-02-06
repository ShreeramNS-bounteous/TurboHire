package com.company.turbohire.backend.services;

import com.company.turbohire.backend.common.SystemLogger;
import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;
import com.company.turbohire.backend.enums.SlotStatus;
import com.company.turbohire.backend.repository.InterviewerProfileRepository;
import com.company.turbohire.backend.repository.InterviewerSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewerService {

    private final InterviewerProfileRepository profileRepository;
    private final InterviewerSlotRepository slotRepository;
    private final SystemLogger systemLogger;

    // ================= CREATE PROFILE =================
    public InterviewerProfile createInterviewerProfile(
            Long userId,
            String expertise,
            String timezone,
            Long actorUserId) {

        InterviewerProfile profile = InterviewerProfile.builder()
                .id(userId)
                .expertise(expertise)
                .timezone(timezone)
                .status("ACTIVE")
                .build();

        profileRepository.save(profile);

        systemLogger.audit(
                actorUserId,
                "CREATE_INTERVIEWER",
                "INTERVIEWER_PROFILE",
                userId
        );

        return profile;
    }

    // ================= ADD SLOT =================
    public InterviewerSlot addInterviewerSlot(
            Long interviewerId,
            LocalDate slotDate,
            LocalTime start,
            LocalTime end,
            Long actorUserId,
            Long hrId
    ) {

        InterviewerProfile profile =
                profileRepository.findById(interviewerId)
                        .orElseThrow(() ->
                                new RuntimeException("Interviewer not found"));

        InterviewerSlot slot = InterviewerSlot.builder()
                .interviewer(profile)
                .slotDate(slotDate)
                .startTime(start)
                .endTime(end)
                .status(SlotStatus.AVAILABLE)

                // these must exist in entity
                .postedByInterviewerId(actorUserId)
                .visibleToHrId(hrId)
                .build();

        slotRepository.save(slot);

        systemLogger.audit(
                actorUserId,
                "SLOT_POSTED",
                "INTERVIEWER_SLOT",
                slot.getId()
        );

        return slot;
    }

    // ================= READ =================

    @Transactional(readOnly = true)
    public List<InterviewerProfile> getAllInterviewerProfiles() {
        return profileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public InterviewerProfile getInterviewerProfile(Long interviewerId) {
        return profileRepository.findById(interviewerId)
                .orElseThrow(() ->
                        new RuntimeException("Interviewer not found"));
    }

    @Transactional(readOnly = true)
    public List<InterviewerSlot> getAllSlots(Long interviewerId) {

        InterviewerProfile interviewer =
                profileRepository.findById(interviewerId)
                        .orElseThrow(() ->
                                new RuntimeException("Interviewer not found"));

        // 🔥 FIXED METHOD NAME
        return slotRepository.findByInterviewerProfile(interviewer);
    }

    @Transactional(readOnly = true)
    public List<InterviewerSlot> getAvailableSlots(Long interviewerId) {

        InterviewerProfile interviewer =
                profileRepository.findById(interviewerId)
                        .orElseThrow(() ->
                                new RuntimeException("Interviewer not found"));

        // 🔥 FIXED METHOD NAME
        return slotRepository.findByInterviewerProfileAndStatus(
                interviewer,
                SlotStatus.AVAILABLE
        );
    }
}

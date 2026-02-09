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

    // Create interviewer profile
    public InterviewerProfile createInterviewerProfile(Long userId, String expertise, String timezone, Long actorUserId) {
        InterviewerProfile profile = InterviewerProfile.builder()
                .id(userId)
                .expertise(expertise)
                .timezone(timezone)
                .status("ACTIVE")
                .build();

        profileRepository.save(profile);
        systemLogger.audit(actorUserId, "CREATE_INTERVIEWER", "INTERVIEWER_PROFILE", userId);
        return profile;
    }

    // List all interviewers
    @Transactional(readOnly = true)
    public List<InterviewerProfile> getAllInterviewerProfiles() {
        return profileRepository.findAll();
    }

    // Get interviewer profile by id
    @Transactional(readOnly = true)
    public InterviewerProfile getInterviewerProfile(Long interviewerId) {
        return profileRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));
    }

    // Add interviewer slot
    public InterviewerSlot addInterviewerSlot(Long interviewerId, LocalDate date, LocalTime start, LocalTime end, Long actorUserId) {
        InterviewerProfile interviewer = profileRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));

        InterviewerSlot slot = InterviewerSlot.builder()
                .interviewer(interviewer)
                .slotDate(date)
                .startTime(start)
                .endTime(end)
                .status(SlotStatus.AVAILABLE)
                .build();

        slotRepository.save(slot);
        systemLogger.audit(actorUserId, "CREATE_SLOT", "INTERVIEWER_SLOT", slot.getId());
        return slot;
    }

    // Remove interviewer slot
    public void removeInterviewerSlot(Long slotId, Long actorUserId) {
        InterviewerSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() == SlotStatus.BOOKED) {
            throw new RuntimeException("Cannot remove a booked slot");
        }

        slotRepository.delete(slot);
        systemLogger.audit(actorUserId, "DELETE_SLOT", "INTERVIEWER_SLOT", slotId);
    }

    // Get all slots for an interviewer
    @Transactional(readOnly = true)
    public List<InterviewerSlot> getAllSlots(Long interviewerId) {
        InterviewerProfile interviewer = profileRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));
        return slotRepository.findByInterviewer(interviewer);
    }

    // Get available slots for an interviewer
    @Transactional(readOnly = true)
    public List<InterviewerSlot> getAvailableSlots(Long interviewerId) {
        InterviewerProfile interviewer = profileRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));
        return slotRepository.findByInterviewerAndStatus(interviewer, SlotStatus.AVAILABLE);
    }
}

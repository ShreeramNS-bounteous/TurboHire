package com.company.turbohire.backend.services;

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

    // READ

    @Transactional(readOnly = true)
    public List<InterviewerProfile> getAllInterviewerProfiles() {
        return profileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<InterviewerSlot> getAvailableInterviewerSlots(Long interviewerId) {

        InterviewerProfile interviewer = profileRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));

        return slotRepository.findByInterviewerAndSlotStatus(
                interviewer,
                SlotStatus.AVAILABLE
        );
    }

    // WRITE

    public Long createInterviewerProfile(Long userId, String expertise, String timezone) {

        InterviewerProfile profile = InterviewerProfile.builder()
                .id(userId)
                .expertise(expertise)
                .timezone(timezone)
                .status("ACTIVE")
                .build();

        profileRepository.save(profile);
        return userId;
    }

    public Long addInterviewerSlot(
            Long interviewerId,
            LocalDate date,
            LocalTime start,
            LocalTime end
    ) {

        InterviewerProfile interviewer = profileRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));

        InterviewerSlot slot = InterviewerSlot.builder()
                .interviewer(interviewer)
                .slotDate(date)
                .startTime(start)
                .endTime(end)
                .slotStatus(SlotStatus.AVAILABLE)
                .build();

        slotRepository.save(slot);
        return slot.getId();
    }

    public void removeInterviewerSlot(Long slotId) {

        InterviewerSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getSlotStatus() == SlotStatus.BOOKED) {
            throw new RuntimeException("Cannot remove a booked slot");
        }

        slotRepository.delete(slot);
    }
}

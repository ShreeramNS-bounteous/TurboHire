package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;
import com.company.turbohire.backend.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewerSlotRepository extends JpaRepository<InterviewerSlot, Long> {

    // Used in InterviewerService.getAvailableInterviewerSlots()
    List<InterviewerSlot> findByInterviewerAndStatus(
            InterviewerProfile interviewer,
            SlotStatus status
    );

    // Useful for frontend (view all slots of an interviewer)
    List<InterviewerSlot> findByInterviewer_Id(Long interviewerId);
}

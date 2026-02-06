package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;
import com.company.turbohire.backend.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewerSlotRepository extends JpaRepository<InterviewerSlot, Long> {

    // Find slots by interviewer and status (available/booked)
    List<InterviewerSlot> findByInterviewerAndStatus(InterviewerProfile interviewer, SlotStatus status);

    // Find all slots of an interviewer
    List<InterviewerSlot> findByInterviewer_Id(Long interviewerId);

    // Optional convenience: find by interviewer entity directly
    default List<InterviewerSlot> findByInterviewer(InterviewerProfile interviewer) {
        return findByInterviewer_Id(interviewer.getId());
    }

    List<InterviewerSlot> findByVisibleToHrIdAndStatus(
            Long hrId,
            SlotStatus status
    );

    List<InterviewerSlot> findByInterviewerProfile(
            InterviewerProfile profile
    );

    List<InterviewerSlot> findByInterviewerProfileAndStatus(
            InterviewerProfile profile,
            SlotStatus status
    );
}

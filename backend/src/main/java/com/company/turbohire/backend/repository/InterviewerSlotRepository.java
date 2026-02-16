package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;
import com.company.turbohire.backend.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface InterviewerSlotRepository extends JpaRepository<InterviewerSlot, Long> {


    List<InterviewerSlot>
    findBySlotDateAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            LocalDate slotDate,
            SlotStatus status,
            LocalTime startTime,
            LocalTime endTime
    );



    // Optional convenience: find by interviewer entity directly
    List<InterviewerSlot> findByUserId(Long userId);

    List<InterviewerSlot> findByUserIdAndStatus(Long userId, SlotStatus status);
}

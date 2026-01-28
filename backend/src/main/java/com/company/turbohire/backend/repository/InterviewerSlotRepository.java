package com.company.turbohire.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;

@Repository
public interface InterviewerSlotRepository extends JpaRepository<InterviewerSlot,Long> {
    List<InterviewerSlot> findByInterviewerAndSlotStatus(
            InterviewerProfile interviewer,
            String slotStatus
    );
}

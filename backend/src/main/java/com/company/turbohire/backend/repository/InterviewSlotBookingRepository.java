package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.InterviewSlotBooking;
import com.company.turbohire.backend.entity.JobRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterviewSlotBookingRepository extends JpaRepository<InterviewSlotBooking,Long> {
    Optional<InterviewSlotBooking> findByInterviewId(Long interviewId);

}

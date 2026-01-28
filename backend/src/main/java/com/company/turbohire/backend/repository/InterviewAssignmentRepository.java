package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewAssignmentRepository extends JpaRepository<InterviewAssignment,Long> {

    // InterviewAssignmentRepository
    boolean existsByInterviewAndInterviewer(Interview interview, User interviewer);


}

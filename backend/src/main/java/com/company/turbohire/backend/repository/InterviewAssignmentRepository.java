package com.company.turbohire.backend.repository;
import java.util.List;

import com.company.turbohire.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.turbohire.backend.entity.Interview;
import com.company.turbohire.backend.entity.InterviewAssignment;
import com.company.turbohire.backend.entity.InterviewAssignmentId;
import com.company.turbohire.backend.entity.User;
@Repository
public interface InterviewAssignmentRepository 
        extends JpaRepository<InterviewAssignment, InterviewAssignmentId> {

    // Example custom method: find all assignments for an interviewer
    List<InterviewAssignment> findByInterviewer(User interviewer);
    
    // Optional: find by interview if needed
    List<InterviewAssignment> findByInterview(Interview interview);
    // InterviewAssignmentRepository
    boolean existsByInterviewAndInterviewer(Interview interview, User interviewer);


}


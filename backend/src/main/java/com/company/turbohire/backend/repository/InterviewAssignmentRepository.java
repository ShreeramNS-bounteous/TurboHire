package com.company.turbohire.backend.repository;
import java.util.List;
import java.util.Optional;

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


    // Optional: find by interview if needed
    List<InterviewAssignment> findByInterview(Interview interview);
    // InterviewAssignmentRepository
    boolean existsByInterviewAndInterviewer(Interview interview, User interviewer);

    Optional<InterviewAssignment> findByInterviewId(Long interviewId);

    List<InterviewAssignment> findByInterviewer_Id(Long interviewerId);

    void deleteByInterview_Id(Long interviewId);



}


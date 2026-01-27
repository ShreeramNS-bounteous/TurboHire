package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.InterviewAssignment;
import com.company.turbohire.backend.entity.Resume;
import com.company.turbohire.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewAssignmentRepository extends JpaRepository<InterviewAssignment,Long> {

}

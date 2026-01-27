package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.InterviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewFeedbackRepository extends JpaRepository<InterviewFeedback,Long> {
}

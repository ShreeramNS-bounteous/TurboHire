package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.InterviewerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewerProfileRepository extends JpaRepository<InterviewerProfile,Long> {
}

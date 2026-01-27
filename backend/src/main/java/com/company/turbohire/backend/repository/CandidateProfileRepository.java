package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile,Long> {

}

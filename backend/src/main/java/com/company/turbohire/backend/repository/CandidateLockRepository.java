package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.CandidateLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateLockRepository extends JpaRepository<CandidateLock,Long> {
}

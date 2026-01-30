package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {

    Optional<Resume> findByCandidate_Id(Long candidateId);
}

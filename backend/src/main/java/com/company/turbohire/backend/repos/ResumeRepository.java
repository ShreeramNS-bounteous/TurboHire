package com.company.turbohire.backend.repos;

import com.company.turbohire.backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume,Long> {
}

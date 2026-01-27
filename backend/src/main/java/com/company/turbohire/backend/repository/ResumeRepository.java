package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume,Long> {

}

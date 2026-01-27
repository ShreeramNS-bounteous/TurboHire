package com.company.turbohire.backend.repository;

import com.company.turbohire.backend.entity.HiringEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HiringEventRepository extends JpaRepository<HiringEvent, Long> {}

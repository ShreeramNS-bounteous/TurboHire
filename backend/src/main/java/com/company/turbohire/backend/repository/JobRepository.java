package com.company.turbohire.backend.repository;
import com.company.turbohire.backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    List<Job> findByStatus(String status);

    List<Job> findByStatusNot(String status);

    // HR sees only jobs of their BU, excluding soft-deleted
    List<Job> findByBusinessUnit_IdAndStatusNot(Long buId, String status);

    Optional<Job> findByIdAndBusinessUnit_Id(Long id, Long buId);

    List<Job> findByBusinessUnit_IdAndStatus(Long buId, String status);

}

package com.company.turbohire.backend.repository;
import com.company.turbohire.backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    List<Job> findByStatus(String status);
    List<Job> findByCreatedBy(Long userId);

    List<Job> findByStatusNot(String status);


}

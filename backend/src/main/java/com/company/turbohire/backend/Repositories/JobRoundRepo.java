package com.company.turbohire.backend.Repositories;
import com.company.turbohire.backend.entity.JobRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRoundRepo extends JpaRepository<JobRound,Long> {
}

package com.company.turbohire.backend.repository;
import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}

package com.company.turbohire.backend.repository;
import com.company.turbohire.backend.entity.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BURepository extends JpaRepository<BusinessUnit,Long> {
}

package com.company.turbohire.backend.Repositories;
import com.company.turbohire.backend.entity.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BURepo extends JpaRepository<BusinessUnit,Long> {
}

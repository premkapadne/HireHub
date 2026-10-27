package com.hirehub.repository;

import com.hirehub.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    boolean existsByIdAndCompanyId(Long id, Long companyId);
}

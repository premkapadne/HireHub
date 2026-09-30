package com.hirehub.repository;

import com.hirehub.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>
{
    List<Company> fetchCompaniesWithJobsByStatus(@Param("status") String status);
}

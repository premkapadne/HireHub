package com.hirehub.repository;

import com.hirehub.entity.HireHubUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HireHubUserRepository extends JpaRepository<HireHubUser, Long>
{
    Optional<HireHubUser> findHireHubUserByEmail(String email);

    Optional<HireHubUser> readUserByEmailOrMobileNumber(String email, String mobileNumber);
}
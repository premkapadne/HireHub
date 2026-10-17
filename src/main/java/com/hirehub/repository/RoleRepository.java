package com.hirehub.repository;

import com.hirehub.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    @Cacheable("roles")
    Optional<Role> findRoleByName(String name);
}
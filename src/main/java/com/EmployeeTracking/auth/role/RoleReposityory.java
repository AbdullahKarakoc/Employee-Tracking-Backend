package com.EmployeeTracking.auth.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleReposityory extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String role);
}

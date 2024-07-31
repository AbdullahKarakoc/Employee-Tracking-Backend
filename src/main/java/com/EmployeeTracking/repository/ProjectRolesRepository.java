package com.EmployeeTracking.repository;

import com.EmployeeTracking.domain.model.ProjectRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRolesRepository extends JpaRepository<ProjectRoles, UUID> {
}

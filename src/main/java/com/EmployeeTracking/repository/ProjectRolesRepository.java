package com.EmployeeTracking.repository;

import com.EmployeeTracking.domain.model.ProjectRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRolesRepository extends JpaRepository<ProjectRoles, UUID> {
    List<ProjectRoles> findByProject_ProjectId(UUID projectId);

    Optional<ProjectRoles> findByProjectRoleIdAndProject_ProjectId(UUID roleId, UUID projectId);
}

package com.EmployeeTracking.repository;

import com.EmployeeTracking.domain.model.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectsRepository extends JpaRepository<Projects, UUID> {
}

package com.EmployeeTracking.repository;

import com.EmployeeTracking.domain.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TasksRepository extends JpaRepository<Tasks, UUID> {
}

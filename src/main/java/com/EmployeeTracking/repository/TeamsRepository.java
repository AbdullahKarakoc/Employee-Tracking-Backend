package com.EmployeeTracking.repository;

import com.EmployeeTracking.domain.model.Teams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamsRepository extends JpaRepository<Teams, UUID> {
}

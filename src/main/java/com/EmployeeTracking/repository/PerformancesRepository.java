package com.EmployeeTracking.repository;

import com.EmployeeTracking.domain.model.Performances;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerformancesRepository extends JpaRepository<Performances, UUID> {
}

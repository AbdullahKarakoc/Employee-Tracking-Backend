package com.EmployeeTracking.repository;

import com.EmployeeTracking.domain.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentsRepository extends JpaRepository<Comments, UUID> {
}

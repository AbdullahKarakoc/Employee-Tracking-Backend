package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProjectRolesResponseDto {

    private UUID projectRoleId;
    private String employeeRole;
    private String description;
    private ProjectsResponseDto project;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProjectRolesResponseDto {

    private UUID projectRolesUUID;
    private String employeeRole;
    private String description;
    private ProjectsResponseDto project;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

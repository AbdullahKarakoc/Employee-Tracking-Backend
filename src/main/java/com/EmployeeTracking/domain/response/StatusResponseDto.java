package com.EmployeeTracking.domain.response;

import com.EmployeeTracking.enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class StatusResponseDto {
    
    private UUID statusUUID;
    private ProjectStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

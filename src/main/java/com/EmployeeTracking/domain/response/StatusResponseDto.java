package com.EmployeeTracking.domain.response;

import com.EmployeeTracking.enums.ProjectStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StatusResponseDto {
    
    private UUID statusId;
    private ProjectStatus status;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProjectsResponseDto {

    private UUID projectUUID;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime deadline;
    private LocalDateTime finishDate;
    private StatusResponseDto status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

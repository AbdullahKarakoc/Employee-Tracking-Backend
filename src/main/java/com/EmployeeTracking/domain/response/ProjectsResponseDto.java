package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProjectsResponseDto {

    private UUID projectId;
    private String name;
    private String description;
    private Instant startDate;
    private Instant deadline;
    private Instant finishDate;
    private StatusResponseDto status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

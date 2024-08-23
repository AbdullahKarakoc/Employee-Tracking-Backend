package com.EmployeeTracking.domain.response;

import com.EmployeeTracking.enums.ProcessStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class TasksResponseDto {
    private UUID taskId;
    private String name;
    private ProcessStatus status;
    private String description;
    private Instant startDate;
    private Instant deadline;
    private Instant finishDate;
    private ProjectsResponseDto project;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

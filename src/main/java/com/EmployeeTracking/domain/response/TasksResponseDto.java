package com.EmployeeTracking.domain.response;

import com.EmployeeTracking.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TasksResponseDto {
    private UUID taskId;
    private String name;
    private TaskStatus status;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime deadline;
    private LocalDateTime finishDate;
    private ProjectsResponseDto project;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

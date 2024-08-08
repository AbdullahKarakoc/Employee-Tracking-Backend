package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CommentsResponseDto {

    private UUID commentId;
    private String title;
    private String description;
    private EmployeeResponseDto employee;
    private TasksResponseDto task;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

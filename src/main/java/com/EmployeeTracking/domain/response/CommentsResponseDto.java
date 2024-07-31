package com.EmployeeTracking.domain.response;

import com.EmployeeTracking.auth.user.EmployeeResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentsResponseDto {

    private UUID commentUUID;
    private String title;
    private String description;
    private EmployeeResponseDto employee;
    private TasksResponseDto task;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

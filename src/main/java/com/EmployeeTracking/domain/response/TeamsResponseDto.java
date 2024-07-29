package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TeamsResponseDto {
    private UUID teamUUID;
    private String teamName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

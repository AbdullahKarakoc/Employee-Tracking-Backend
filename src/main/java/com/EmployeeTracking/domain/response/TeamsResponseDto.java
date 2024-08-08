package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TeamsResponseDto {
    private UUID teamId;
    private String teamName;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

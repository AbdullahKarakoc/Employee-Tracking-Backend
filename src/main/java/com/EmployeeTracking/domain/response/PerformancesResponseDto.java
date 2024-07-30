package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PerformancesResponseDto {

    private UUID performanceUUID;
    private int totalPoint;
    private int commentAmount;
    private int completedTask;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

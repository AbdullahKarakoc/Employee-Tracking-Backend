package com.EmployeeTracking.domain.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PerformancesResponseDto {

    private UUID performanceId;
    private int totalPoint;
    private int commentAmount;
    private int completedTask;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

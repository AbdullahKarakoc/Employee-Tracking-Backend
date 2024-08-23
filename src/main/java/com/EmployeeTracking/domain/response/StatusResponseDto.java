package com.EmployeeTracking.domain.response;

import com.EmployeeTracking.enums.ProcessStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class StatusResponseDto {
    
    private UUID statusId;
    private ProcessStatus status;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}

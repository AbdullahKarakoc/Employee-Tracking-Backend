package com.EmployeeTracking.domain.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PerformancesRequestDto {

    @NotNull(message = "Total points are required")
    @Positive(message = "Total points must be a positive number")
    private Integer totalPoint;

    @NotNull(message = "Comment amount is required")
    @Positive(message = "Comment amount must be a positive number")
    private Integer commentAmount;

    @NotNull(message = "Completed task amount is required")
    @Positive(message = "Completed task amount must be a positive number")
    private Integer completedTask;
}

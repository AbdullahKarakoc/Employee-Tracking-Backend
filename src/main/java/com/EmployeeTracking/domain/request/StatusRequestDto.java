package com.EmployeeTracking.domain.request;

import com.EmployeeTracking.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StatusRequestDto {

    @NotNull(message = "Project Status is required")
    private ProjectStatus status;

    @Size(max = 250, message = "Project Description must be less than 250 characters")
    private String description;

}

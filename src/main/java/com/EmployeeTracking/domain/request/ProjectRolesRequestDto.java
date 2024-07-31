package com.EmployeeTracking.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectRolesRequestDto {

    @NotBlank(message = "Employee role is required")
    @Size(min = 1, max = 25, message = "Employee role name must be between 1 and 50 characters")
    private String employeeRole;

    @Size(max = 250, message = "Project role description must be less than 250 characters")
    private String description;
}

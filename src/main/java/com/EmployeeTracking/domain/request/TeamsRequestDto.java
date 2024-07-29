package com.EmployeeTracking.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamsRequestDto {

    @NotBlank(message = "Team name is required")
    @Size(min = 1, max = 25, message = "Team name must be between 1 and 50 characters")
    private String teamName;

    @Size(max = 250, message = "Description must be less than 250 characters")
    private String description;
}

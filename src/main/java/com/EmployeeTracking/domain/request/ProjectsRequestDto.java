package com.EmployeeTracking.domain.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ProjectsRequestDto {

    @NotBlank(message = "Project name is required")
    @Size(min = 1, max = 25, message = "Team name must be between 1 and 25 characters")
    private String name;

    @Size(max = 250, message = "Project description must be less than 250 characters")
    private String description;

    @NotNull(message = "Project startDate is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant startDate;

    @NotNull(message = "Project deadline is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant deadline;

    @NotNull(message = "Project finishDate is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant finishDate;

    @Valid
    @NotNull(message = "Project Status is required")
    private StatusRequestDto status;

}

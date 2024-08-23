package com.EmployeeTracking.domain.request;

import com.EmployeeTracking.enums.ProcessStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.UUID;

@Data
public class TasksRequestDto {

    @NotBlank(message = "Task name is required")
    @Size(min = 1, max = 100, message = "Task name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Task status is required")
    private ProcessStatus status;

    @Size(max = 250, message = "Task description must be less than 250 characters")
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

    @NotNull(message = "Project ID is required")
    private UUID projectId;
}

package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.TasksRequestDto;
import com.EmployeeTracking.domain.response.TasksResponseDto;
import com.EmployeeTracking.service.TasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks-Controller", description = "Controller managing operations related to tasks")
@SecurityRequirement(name = "bearerAuth")
public class TasksController {

    private final TasksService tasksService;

    @Operation(
            summary = "Save a new task",
            description = "An endpoint used to save a new task.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping
    public ResponseEntity<TasksResponseDto> saveTask(@Valid @RequestBody TasksRequestDto taskRequestDto) {
        TasksResponseDto savedTask = tasksService.saveTask(taskRequestDto);
        return ResponseEntity.ok(savedTask);
    }

    @Operation(
            summary = "Get all tasks",
            description = "An endpoint used to list all tasks.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<TasksResponseDto>> getAllTasks() {
        List<TasksResponseDto> allTasks = tasksService.getAllTasks();
        return ResponseEntity.ok(allTasks);
    }

    @Operation(
            summary = "Get task by ID",
            description = "An endpoint used to get details of a task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TasksResponseDto> getTaskById(@PathVariable UUID id) {
        TasksResponseDto task = tasksService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @Operation(
            summary = "Update task",
            description = "An endpoint used to update an existing task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TasksResponseDto> updateTask(@Valid @PathVariable UUID id, @RequestBody @Valid TasksRequestDto taskRequestDto) {
        TasksResponseDto updatedTask = tasksService.updateTask(id, taskRequestDto);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(
            summary = "Delete task",
            description = "An endpoint used to delete an existing task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        tasksService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}

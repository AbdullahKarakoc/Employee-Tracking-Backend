package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.service.ProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects-Controller", description = "Controller managing operations related to projects")
@SecurityRequirement(name = "bearerAuth")
public class ProjectsController {

    @Autowired
    private ProjectsService projectsService;

    @Operation(
            summary = "Save a new project",
            description = "An endpoint used to save a new project.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping
    public ResponseEntity<ProjectsResponseDto> saveProject(@Valid @RequestBody ProjectsRequestDto projectRequestDto) {
        ProjectsResponseDto savedProject = projectsService.saveProject(projectRequestDto);
        return ResponseEntity.ok(savedProject);
    }

    @Operation(
            summary = "Get all projects",
            description = "An endpoint used to list all projects.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<ProjectsResponseDto>> getAllProjects() {
        List<ProjectsResponseDto> allProjects = projectsService.getAllProjects();
        return ResponseEntity.ok(allProjects);
    }

    @Operation(
            summary = "Get project by ID",
            description = "An endpoint used to get details of a project by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved project"),
                    @ApiResponse(responseCode = "404", description = "Project not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProjectsResponseDto> getProjectById(@PathVariable UUID id) {
        ProjectsResponseDto project = projectsService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @Operation(
            summary = "Update project",
            description = "An endpoint used to update an existing project by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Project not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProjectsResponseDto> updateProject(@Valid @PathVariable UUID id, @RequestBody ProjectsRequestDto projectRequestDto) {
        ProjectsResponseDto updatedProject = projectsService.updateProject(id, projectRequestDto);
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(
            summary = "Delete project",
            description = "An endpoint used to delete an existing project by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Project not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectsService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}

package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.ProjectRolesRequestDto;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.response.ProjectRolesResponseDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.service.ProjectRolesService;
import com.EmployeeTracking.service.ProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
@Tag(name = "Projects-Controller", description = "Controller managing operations related to projects and project roles")
@SecurityRequirement(name = "bearerAuth")
public class ProjectsController {

    private final ProjectsService projectsService;
    private final ProjectRolesService projectRolesService;

    // Projects Endpoints
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
    public ResponseEntity<ProjectsResponseDto> updateProject(@Valid @PathVariable UUID id, @RequestBody @Valid ProjectsRequestDto projectRequestDto) {
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

    // Project Roles Endpoints
    @Operation(
            summary = "Save a new project role",
            description = "An endpoint used to save a new project role under a specific project.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project role successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping("/{projectId}/roles")
    public ResponseEntity<ProjectRolesResponseDto> saveProjectRole(
            @PathVariable UUID projectId,
            @Valid @RequestBody ProjectRolesRequestDto projectRolesRequestDto) {
        ProjectRolesResponseDto savedProjectRole = projectRolesService.saveProjectRole(projectId, projectRolesRequestDto);
        return ResponseEntity.ok(savedProjectRole);
    }

    @Operation(
            summary = "Get all project roles",
            description = "An endpoint used to list all roles under a specific project.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{projectId}/roles")
    public ResponseEntity<List<ProjectRolesResponseDto>> getAllProjectRoles(@PathVariable UUID projectId) {
        List<ProjectRolesResponseDto> allProjectRoles = projectRolesService.getAllProjectRoles(projectId);
        return ResponseEntity.ok(allProjectRoles);
    }

    @Operation(
            summary = "Get project role by ID",
            description = "An endpoint used to get details of a project role by its ID under a specific project.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved project role"),
                    @ApiResponse(responseCode = "404", description = "Project role not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{projectId}/roles/{roleId}")
    public ResponseEntity<ProjectRolesResponseDto> getProjectRoleById(
            @PathVariable UUID projectId, @PathVariable UUID roleId) {
        ProjectRolesResponseDto projectRole = projectRolesService.getProjectRoleById(projectId, roleId);
        return ResponseEntity.ok(projectRole);
    }

    @Operation(
            summary = "Update project role",
            description = "An endpoint used to update an existing project role by its ID under a specific project.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project role successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Project role not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{projectId}/roles/{roleId}")
    public ResponseEntity<ProjectRolesResponseDto> updateProjectRole(
            @PathVariable UUID projectId,
            @PathVariable UUID roleId,
            @Valid @RequestBody ProjectRolesRequestDto projectRolesRequestDto) {
        ProjectRolesResponseDto updatedProjectRole = projectRolesService.updateProjectRole(projectId, roleId, projectRolesRequestDto);
        return ResponseEntity.ok(updatedProjectRole);
    }

    @Operation(
            summary = "Delete project role",
            description = "An endpoint used to delete an existing project role by its ID under a specific project.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project role successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Project role not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{projectId}/roles/{roleId}")
    public ResponseEntity<Void> deleteProjectRole(@PathVariable UUID projectId, @PathVariable UUID roleId) {
        projectRolesService.deleteProjectRole(projectId, roleId);
        return ResponseEntity.ok().build();
    }
}


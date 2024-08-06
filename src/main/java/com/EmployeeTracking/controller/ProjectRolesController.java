package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.ProjectRolesRequestDto;
import com.EmployeeTracking.domain.response.ProjectRolesResponseDto;
import com.EmployeeTracking.service.ProjectRolesService;
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
@RequestMapping("/project-roles")
@Tag(name = "ProjectRoles-Controller", description = "Controller managing operations related to project roles")
@SecurityRequirement(name = "bearerAuth")
public class ProjectRolesController {

    private final ProjectRolesService projectRolesService;

    @Operation(
            summary = "Save a new project role",
            description = "An endpoint used to save a new project role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project role successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping
    public ResponseEntity<ProjectRolesResponseDto> saveProjectRole(@Valid @RequestBody ProjectRolesRequestDto projectRolesRequestDto) {
        ProjectRolesResponseDto savedProjectRole = projectRolesService.saveProjectRole(projectRolesRequestDto);
        return ResponseEntity.ok(savedProjectRole);
    }

    @Operation(
            summary = "Get all project roles",
            description = "An endpoint used to list all project roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<ProjectRolesResponseDto>> getAllProjectRoles() {
        List<ProjectRolesResponseDto> allProjectRoles = projectRolesService.getAllProjectRoles();
        return ResponseEntity.ok(allProjectRoles);
    }

    @Operation(
            summary = "Get project role by ID",
            description = "An endpoint used to get details of a project role by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved project role"),
                    @ApiResponse(responseCode = "404", description = "Project role not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProjectRolesResponseDto> getProjectRoleById(@PathVariable UUID id) {
        ProjectRolesResponseDto projectRole = projectRolesService.getProjectRoleById(id);
        return ResponseEntity.ok(projectRole);
    }

    @Operation(
            summary = "Update project role",
            description = "An endpoint used to update an existing project role by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project role successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Project role not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProjectRolesResponseDto> updateProjectRole(@Valid @PathVariable UUID id, @RequestBody ProjectRolesRequestDto projectRolesRequestDto) {
        ProjectRolesResponseDto updatedProjectRole = projectRolesService.updateProjectRole(id, projectRolesRequestDto);
        return ResponseEntity.ok(updatedProjectRole);
    }

    @Operation(
            summary = "Delete project role",
            description = "An endpoint used to delete an existing project role by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project role successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Project role not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectRole(@PathVariable UUID id) {
        projectRolesService.deleteProjectRole(id);
        return ResponseEntity.ok().build();
    }
}
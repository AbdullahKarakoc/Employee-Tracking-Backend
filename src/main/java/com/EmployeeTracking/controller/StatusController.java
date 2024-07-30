package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.StatusRequestDto;
import com.EmployeeTracking.domain.response.StatusResponseDto;
import com.EmployeeTracking.service.StatusService;
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
@RequestMapping("/status")
@Tag(name = "Status-Controller", description = "Controller managing operations related to project status")
@SecurityRequirement(name = "bearerAuth")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @Operation(
            summary = "Save a new status",
            description = "An endpoint used to save a new status.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping
    public ResponseEntity<StatusResponseDto> saveStatus(@Valid @RequestBody StatusRequestDto statusRequestDto) {
        StatusResponseDto savedStatus = statusService.saveStatus(statusRequestDto);
        return ResponseEntity.ok(savedStatus);
    }

    @Operation(
            summary = "Get all statuses",
            description = "An endpoint used to list all statuses.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<StatusResponseDto>> getAllStatuses() {
        List<StatusResponseDto> allStatuses = statusService.getAllStatuses();
        return ResponseEntity.ok(allStatuses);
    }

    @Operation(
            summary = "Get status by ID",
            description = "An endpoint used to get details of a status by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved status"),
                    @ApiResponse(responseCode = "404", description = "Status not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDto> getStatusById(@PathVariable UUID id) {
        StatusResponseDto status = statusService.getStatusById(id);
        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Update status",
            description = "An endpoint used to update an existing status by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Status not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponseDto> updateStatus(@Valid @PathVariable UUID id, @RequestBody StatusRequestDto statusRequestDto) {
        StatusResponseDto updatedStatus = statusService.updateStatus(id, statusRequestDto);
        return ResponseEntity.ok(updatedStatus);
    }

    @Operation(
            summary = "Delete status",
            description = "An endpoint used to delete an existing status by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Status not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable UUID id) {
        statusService.deleteStatus(id);
        return ResponseEntity.ok().build();
    }
}


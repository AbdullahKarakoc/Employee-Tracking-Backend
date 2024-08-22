package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.PerformancesRequestDto;
import com.EmployeeTracking.domain.response.PerformancesResponseDto;
import com.EmployeeTracking.service.PerformancesService;
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
@RequestMapping("/performances")
@Tag(name = "Performances-Controller", description = "Controller managing operations related to performances")
@SecurityRequirement(name = "bearerAuth")
public class PerformancesController {

    private final PerformancesService performancesService;

    @Operation(
            summary = "Save a new performance",
            description = "An endpoint used to save a new performance.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Performance successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping
    public ResponseEntity<PerformancesResponseDto> savePerformance(@Valid @RequestBody PerformancesRequestDto performancesRequestDto) {
        PerformancesResponseDto savedPerformance = performancesService.savePerformance(performancesRequestDto);
        return ResponseEntity.ok(savedPerformance);
    }

    @Operation(
            summary = "Get all performances",
            description = "An endpoint used to list all performances.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<PerformancesResponseDto>> getAllPerformances() {
        List<PerformancesResponseDto> allPerformances = performancesService.getAllPerformances();
        return ResponseEntity.ok(allPerformances);
    }

    @Operation(
            summary = "Get performance by ID",
            description = "An endpoint used to get details of a performance by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved performance"),
                    @ApiResponse(responseCode = "404", description = "Performance not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PerformancesResponseDto> getPerformanceById(@PathVariable UUID id) {
        PerformancesResponseDto performance = performancesService.getPerformanceById(id);
        return ResponseEntity.ok(performance);
    }

    @Operation(
            summary = "Update performance",
            description = "An endpoint used to update an existing performance by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Performance successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Performance not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PerformancesResponseDto> updatePerformance(@Valid @PathVariable UUID id, @RequestBody @Valid PerformancesRequestDto performancesRequestDto) {
        PerformancesResponseDto updatedPerformance = performancesService.updatePerformance(id, performancesRequestDto);
        return ResponseEntity.ok(updatedPerformance);
    }

    @Operation(
            summary = "Delete performance",
            description = "An endpoint used to delete an existing performance by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Performance successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Performance not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable UUID id) {
        performancesService.deletePerformance(id);
        return ResponseEntity.ok().build();
    }
}

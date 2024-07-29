package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.TeamsRequestDto;
import com.EmployeeTracking.domain.response.TeamsResponseDto;
import com.EmployeeTracking.service.TeamsService;
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
@RequestMapping("/teams")
@Tag(name = "Teams-Controller", description = "Controller managing operations related to teams")
@SecurityRequirement(name = "bearerAuth")
public class TeamsController {
    @Autowired
    private TeamsService teamService;


    @Operation(
            summary = "Save a new team",
            description = "An endpoint used to save a new team.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Team successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping
    public ResponseEntity<TeamsResponseDto> saveTeam(@Valid @RequestBody TeamsRequestDto teamRequestDto) {
        TeamsResponseDto savedTeam = teamService.saveTeam(teamRequestDto);
        return ResponseEntity.ok(savedTeam);
    }


    @Operation(
            summary = "Get all teams",
            description = "An endpoint used to list all teams.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<TeamsResponseDto>> getAllTeams() {
        List<TeamsResponseDto> allTeams = teamService.getAllTeams();
        return ResponseEntity.ok(allTeams);
    }

    @Operation(
            summary = "Get team by ID",
            description = "An endpoint used to get details of a team by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved team"),
                    @ApiResponse(responseCode = "404", description = "Team not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TeamsResponseDto> getTeamById(@PathVariable UUID id) {
        TeamsResponseDto team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }



    @Operation(
            summary = "Update team",
            description = "An endpoint used to update an existing team by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Team successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Team not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TeamsResponseDto> updateTeam(@Valid @PathVariable UUID id, @RequestBody TeamsRequestDto teamRequestDto) {
        TeamsResponseDto updatedTeam = teamService.updateTeam(id, teamRequestDto);
        return ResponseEntity.ok(updatedTeam);
    }

    @Operation(
            summary = "Delete team",
            description = "An endpoint used to delete an existing team by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Team successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Team not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }
}
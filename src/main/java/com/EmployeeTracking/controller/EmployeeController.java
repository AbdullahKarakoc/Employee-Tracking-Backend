package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.service.EmployeeService;
import com.EmployeeTracking.domain.response.EmployeeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Get all users",
            description = "An endpoint used to list all users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllUsers() {
        List<EmployeeResponseDto> allUsers = employeeService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @Operation(
            summary = "Get User by ID",
            description = "An endpoint used to get details of a User by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved User"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getUserById(@PathVariable UUID id) {
        EmployeeResponseDto user = employeeService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    @Operation(
            summary = "Get User details",
            description = "An endpoint used to get details of logged user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved User"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDto> getMyDetails() {
        EmployeeResponseDto user = employeeService.getLoggedInUserDetails();
        return ResponseEntity.ok(user);
    }


    @Operation(
            summary = "Update User",
            description = "An endpoint used to update an existing User by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully updated"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateUser(@Valid @PathVariable UUID id, @RequestBody @Valid CompleteRegisterDto completeRegisterDto) {
        EmployeeResponseDto updatedUser = employeeService.updateUser(id, completeRegisterDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Delete User",
            description = "An endpoint used to delete an existing User by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        employeeService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

package com.EmployeeTracking.domain.response;

import com.EmployeeTracking.domain.model.Role;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class EmployeeResponseDto {

    private UUID employeeId;
    private String firstname;
    private String lastname;
    private Instant dateOfBirth;
    @Column(unique = true)
    private String email;
    private List<RoleResponseDto> roles;
    private Instant createdAt;
    private Instant updatedAt;
}
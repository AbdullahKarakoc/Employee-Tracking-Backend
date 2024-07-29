package com.EmployeeTracking.auth.user;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class EmployeeResponseDto {

    private UUID userUUID;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
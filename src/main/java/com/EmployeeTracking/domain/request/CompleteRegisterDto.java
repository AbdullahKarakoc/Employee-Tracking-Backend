package com.EmployeeTracking.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;


@Data
public class CompleteRegisterDto {

    @NotBlank(message = "Firstname is mandatory")
    @Size(min = 1, max = 25, message = "First name must be between 1 and 25 characters")
    private String firstName;

    @NotBlank(message = "Lastname is mandatory")
    @Size(min = 1, max = 25, message = "Last name must be between 1 and 25 characters")
    private String lastName;

    @NotNull(message = "Birth date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant dateOfBirth;

    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Size(min = 6, max = 6, message = "Activation code must be exactly 6 characters long")
    @NotBlank(message = "Activation Code is required")
    private String activationCode;

}

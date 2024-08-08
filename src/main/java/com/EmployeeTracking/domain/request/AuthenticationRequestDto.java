package com.EmployeeTracking.domain.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthenticationRequestDto {
    @Email(message = "Email is not well formatted")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}

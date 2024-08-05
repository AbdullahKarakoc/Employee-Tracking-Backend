package com.EmployeeTracking.auth._auth;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String password;
    private String activationCode;

}

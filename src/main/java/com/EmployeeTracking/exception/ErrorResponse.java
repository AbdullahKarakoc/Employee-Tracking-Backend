package com.EmployeeTracking.exception;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponse {

    private int errorCode;
    private String errorMessage;
    private String details;
    private LocalDateTime timestamp;
    private String message;

    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

}

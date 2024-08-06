package com.EmployeeTracking.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private int statusCode;
    private String error;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private Integer businessErrorCode;
    private String businessErrorDescription;
    private Set<String> validationErrors;
    private Map<String, String> errors;

    public ErrorResponse(int statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }
}

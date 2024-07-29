package com.EmployeeTracking.auth.handler;

public class ActivationTokenExpiredException extends RuntimeException {
    public ActivationTokenExpiredException(String message) {
        super(message);
    }
}

package com.EmployeeTracking.enums;

public enum UserRole {


    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_USER("SUPER_USER");



    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

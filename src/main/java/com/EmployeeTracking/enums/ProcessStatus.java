package com.EmployeeTracking.enums;

public enum ProcessStatus {

    NOT_STARTED("NOT STARTED"),
    IN_PROGRESS("IN PROGRESS"),
    COMPLETED("COMPLETED"),
    ON_HOLD("ON HOLD"),
    CANCELLED("CANCELLED");

    private final String value;

    ProcessStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

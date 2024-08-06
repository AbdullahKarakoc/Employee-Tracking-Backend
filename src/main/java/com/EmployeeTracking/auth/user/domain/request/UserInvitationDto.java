package com.EmployeeTracking.auth.user.domain.request;

import lombok.Data;

@Data
public class UserInvitationDto {
    private String email;
    private String role;

}

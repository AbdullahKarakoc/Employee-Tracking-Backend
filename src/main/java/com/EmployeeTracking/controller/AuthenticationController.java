package com.EmployeeTracking.controller;

import com.EmployeeTracking.service.AuthenticationService;
import com.EmployeeTracking.domain.request.AuthenticationRequestDto;
import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.domain.request.UserInvitationDto;
import com.EmployeeTracking.domain.response.AuthenticationResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @PreAuthorize("hasAuthority('SUPER_USER')")
    @PostMapping("/invite")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, Boolean>> inviteUser(@RequestBody @Valid UserInvitationDto request) throws MessagingException {
        service.inviteUser(request);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.accepted().body(response);
    }

    @PostMapping("/complete-register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, Boolean>> register(@RequestBody @Valid CompleteRegisterDto request) {
        service.registerUser(request);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.accepted().body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid AuthenticationRequestDto request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<String> confirm(@RequestParam String activationCode) throws MessagingException {
        String redirectUrl = service.activateAccount(activationCode);
        return ResponseEntity.ok(redirectUrl);
    }
}

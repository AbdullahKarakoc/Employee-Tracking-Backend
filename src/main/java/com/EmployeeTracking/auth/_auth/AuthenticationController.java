package com.EmployeeTracking.auth._auth;

import com.EmployeeTracking.auth.user.domain.request.RegisterDto;
import com.EmployeeTracking.auth.user.domain.request.UserInvitationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @PreAuthorize("hasAuthority('SUPER_USER')")
    @PostMapping("/invite")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> inviteUser(@RequestBody @Valid UserInvitationDto request) throws MessagingException {
        service.inviteUser(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto request) {
        service.registerUser(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<String> confirm(@RequestParam String activationCode) throws MessagingException {
        String redirectUrl = service.activateAccount(activationCode);
        return ResponseEntity.ok(redirectUrl);
    }
}

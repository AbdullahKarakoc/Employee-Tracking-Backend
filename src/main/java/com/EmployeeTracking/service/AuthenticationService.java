package com.EmployeeTracking.service;

import com.EmployeeTracking.email.EmailService;
import com.EmployeeTracking.email.EmailTemplateName;
import com.EmployeeTracking.exception.ActivationTokenExpiredException;
import com.EmployeeTracking.exception.InvalidTokenException;
import com.EmployeeTracking.exception.UserAlreadyExistsException;
import com.EmployeeTracking.domain.request.AuthenticationRequestDto;
import com.EmployeeTracking.domain.response.AuthenticationResponseDto;
import com.EmployeeTracking.repository.RoleRepository;
import com.EmployeeTracking.security.JwtService;
import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.domain.request.UserInvitationDto;
import com.EmployeeTracking.repository.EmployeeRepository;
import com.EmployeeTracking.domain.model.Token;
import com.EmployeeTracking.repository.TokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;


    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    public void inviteUser(UserInvitationDto userInvitationDto) throws MessagingException {

        if (employeeRepository.findByEmail(userInvitationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with email %s already exists", userInvitationDto.getEmail()));
        }

        var role = roleRepository.findByName(userInvitationDto.getRole())
                .orElseThrow(() -> new IllegalStateException(String.format("Role %s was not initiated", userInvitationDto.getRole())));

        var employee = new Employee();
        employee.setEmail(userInvitationDto.getEmail());
        employee.setEnabled(false);
        employee.setRoles(List.of(role));

        employeeRepository.save(employee);
        sendValidationEmail(employee);
    }



    public void registerUser(CompleteRegisterDto completeRegisterDto) {
        Token token = tokenRepository.findByToken(completeRegisterDto.getActivationCode())
                .orElseThrow(() -> new InvalidTokenException("Invalid activation code"));

        if (Instant.now().isAfter(token.getExpiresAt())) {
            throw new ActivationTokenExpiredException("Activation code has expired");
        }

        Employee employee = token.getEmployee();
        employee.setFirstname(completeRegisterDto.getFirstName());
        employee.setLastname(completeRegisterDto.getLastName());
        employee.setDateOfBirth(completeRegisterDto.getDateOfBirth());
        employee.setPassword(passwordEncoder.encode(completeRegisterDto.getPassword()));
        employee.setEnabled(true);

        employeeRepository.save(employee);

        token.setValidatedAt(Instant.now());
        tokenRepository.save(token);
    }




    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var employee = ((Employee) auth.getPrincipal());
        claims.put("fullName", employee.getFullName());

        var jwtToken = jwtService.generateToken(claims, (Employee) auth.getPrincipal());

        AuthenticationResponseDto response = new AuthenticationResponseDto();
        response.setToken(jwtToken);
        return response;
    }


    public String  activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        if (Instant.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getEmployee());
            throw new ActivationTokenExpiredException("Activation token has expired. A new token has been sent to the same email address");
        }

        var employee = employeeRepository.findById(savedToken.getEmployee().getEmployeeId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        employee.setEnabled(true);
        employeeRepository.save(employee);

        savedToken.setValidatedAt(Instant.now());
        tokenRepository.save(savedToken);

        return "http://localhost:3000/complete-registration?activationCode=" + token;
    }

    private String generateAndSaveActivationToken(Employee employee) {

        String generatedToken = generateActivationCode(6);
        var token = new Token();
        token.setToken(generatedToken);
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plus(Duration.ofMinutes(15)));
        token.setEmployee(employee);
        tokenRepository.save(token);

        return generatedToken;
    }


    private void sendValidationEmail(Employee employee) throws MessagingException {
        var activationCode = generateAndSaveActivationToken(employee);

        emailService.sendEmail(
                employee.getEmail(),
                employee.getFirstname() + " " + employee.getLastname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl + "?activationCode=" + activationCode,
                activationCode,
                "Account activation"
        );
    }


    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

}

package com.EmployeeTracking.auth._auth;

import com.EmployeeTracking.auth.email.EmailService;
import com.EmployeeTracking.auth.email.EmailTemplateName;
import com.EmployeeTracking.auth.handler.ActivationTokenExpiredException;
import com.EmployeeTracking.auth.handler.InvalidTokenException;
import com.EmployeeTracking.auth.handler.UserAlreadyExistsException;
import com.EmployeeTracking.auth.role.RoleRepository;
import com.EmployeeTracking.auth.security.JwtService;
import com.EmployeeTracking.auth.user.Employee;
import com.EmployeeTracking.auth.user.EmployeeRepository;
import com.EmployeeTracking.auth.user.Token;
import com.EmployeeTracking.auth.user.TokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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

    public void register(RegistrationRequest request) throws MessagingException {
        // Check if the user already exists
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User with email" + request.getEmail() + "already exist");
        }

        // Get the USER role
        var employeeRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        // Create new user
        var employee = Employee.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(employeeRole))
                .build();

        // Save user and send validation email
        employeeRepository.save(employee);
        sendValidationEmail(employee);
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getEmployee());
            throw new ActivationTokenExpiredException("Activation token has expired. A new token has been sent to the same email address");
        }

        var employee = employeeRepository.findById(savedToken.getEmployee().getUserUUID())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        employee.setEnabled(true);
        employeeRepository.save(employee);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }


    private String generateAndSaveActivationToken(Employee employee) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .employee(employee)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }


    private void sendValidationEmail(Employee employee) throws MessagingException {
        var newToken = generateAndSaveActivationToken(employee);

        emailService.sendEmail(
                employee.getEmail(),
                employee.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
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

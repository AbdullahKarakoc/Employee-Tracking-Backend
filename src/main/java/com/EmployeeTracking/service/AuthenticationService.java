package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Role;
import com.EmployeeTracking.email.EmailService;
import com.EmployeeTracking.email.EmailTemplateName;
import com.EmployeeTracking.exception.ActivationTokenExpiredException;
import com.EmployeeTracking.exception.UserAlreadyExistsException;
import com.EmployeeTracking.domain.request.AuthenticationRequestDto;
import com.EmployeeTracking.domain.response.AuthenticationResponseDto;
import com.EmployeeTracking.security.JwtService;
import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.domain.request.UserInvitationDto;
import com.EmployeeTracking.domain.model.Token;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmployeeService employeeService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Value("${application.mailing.frontend.complete-registration-url}")
    private String completeRegistrationUrl;

    @Transactional
    public void inviteUser(UserInvitationDto userInvitationDto) throws MessagingException {
        Objects.requireNonNull(userInvitationDto, "User invitation details cannot be null");

        Employee existingEmployee = employeeService.findByEmail(userInvitationDto.getEmail());

        if (nonNull(existingEmployee)) {
            throw new UserAlreadyExistsException(String.format("User with email %s already exists", userInvitationDto.getEmail()));
        }

        Role role = roleService.findByName(userInvitationDto.getRole());

        Employee employee = new Employee();
        employee.setEmail(userInvitationDto.getEmail());
        employee.setEnabled(false);
        employee.setRoles(List.of(role));

        employeeService.save(employee);
        sendValidationEmail(employee);
    }


    @Transactional
    public void registerUser(CompleteRegisterDto completeRegisterDto) {
        Objects.requireNonNull(completeRegisterDto, "Complete register details cannot be null");

        Token token = tokenService.findByToken(completeRegisterDto.getActivationCode());

        if (Instant.now().isAfter(token.getExpiresAt())) {
            throw new ActivationTokenExpiredException("Activation code has expired");
        }

        Employee employee = token.getEmployee();
        employee.setFirstname(completeRegisterDto.getFirstName());
        employee.setLastname(completeRegisterDto.getLastName());
        employee.setDateOfBirth(completeRegisterDto.getDateOfBirth());
        employee.setPhone(completeRegisterDto.getPhone());
        employee.setPassword(passwordEncoder.encode(completeRegisterDto.getPassword()));
        employee.setEnabled(true);

        employeeService.save(employee);

        token.setValidatedAt(Instant.now());
        tokenService.save(token);
    }



    @Transactional
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        Objects.requireNonNull(request, "Authentication request details cannot be null");

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        HashMap<String, Object> claims =new HashMap<>();
        Employee employee = ((Employee) auth.getPrincipal());
        claims.put("fullName", employee.getFullName());

        String jwtToken = jwtService.generateToken(claims, (Employee) auth.getPrincipal());

        AuthenticationResponseDto response = new AuthenticationResponseDto();
        response.setToken(jwtToken);
        return response;
    }


    @Transactional
    public String  activateAccount(String token) throws MessagingException {
        Objects.requireNonNull(token, "Activation token cannot be null");

        Token savedToken = tokenService.findByToken(token);

        if (Instant.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getEmployee());
            throw new ActivationTokenExpiredException("Activation token has expired. A new token has been sent to the same email address");
        }

        Employee employee = employeeService.findById(savedToken.getEmployee().getEmployeeId());
        employee.setEnabled(true);
        employeeService.save(employee);

        savedToken.setValidatedAt(Instant.now());
        tokenService.save(savedToken);

        return completeRegistrationUrl + "?activationCode=" + token;
    }


    private String generateAndSaveActivationToken(Employee employee) {

        String generatedToken = generateActivationCode(6);
        Token token = new Token();
        token.setToken(generatedToken);
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plus(Duration.ofMinutes(15)));
        token.setEmployee(employee);
        tokenService.save(token);

        return generatedToken;
    }


    private void sendValidationEmail(Employee employee) throws MessagingException {
        String activationCode = generateAndSaveActivationToken(employee);

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

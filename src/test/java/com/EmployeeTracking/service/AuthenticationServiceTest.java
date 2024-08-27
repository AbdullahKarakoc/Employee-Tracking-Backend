package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.model.Token;
import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.domain.request.AuthenticationRequestDto;
import com.EmployeeTracking.domain.request.UserInvitationDto;
import com.EmployeeTracking.domain.response.AuthenticationResponseDto;
import com.EmployeeTracking.email.EmailService;
import com.EmployeeTracking.exception.ActivationTokenExpiredException;
import com.EmployeeTracking.exception.UserAlreadyExistsException;
import com.EmployeeTracking.security.JwtService;
import com.EmployeeTracking.util.TestDataFactory;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

class AuthenticationServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private RoleService roleService;

    @Mock
    private EmailService emailService;

    @Mock
    private TokenService tokenService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Employee employee;
    private Token token;
    private CompleteRegisterDto completeRegisterDto;
    private AuthenticationRequestDto authenticationRequestDto;
    private UserInvitationDto userInvitationDto;
    private Authentication authentication;

    @BeforeEach
    void setUp() throws MessagingException {
        MockitoAnnotations.openMocks(this);

        employee = TestDataFactory.createEmployee();
        token = TestDataFactory.createToken();
        completeRegisterDto = TestDataFactory.createCompleteRegisterDto();
        authenticationRequestDto = TestDataFactory.createAuthenticationRequestDto();
        userInvitationDto = TestDataFactory.createUserInvitationDto();
        authentication = mock(Authentication.class);
        doNothing().when(emailService).sendEmail(
                anyString(), anyString(), any(), anyString(), anyString(), anyString()
        );

        when(tokenService.findByToken(anyString())).thenReturn(token);
        when(employeeService.save(any(Employee.class))).thenReturn(employee);
        when(tokenService.save(any(Token.class))).thenReturn(token);
    }

    @Test
    void testInviteUser_whenUserDoesNotExist() throws MessagingException {
        when(employeeService.findByEmail(userInvitationDto.getEmail())).thenReturn(null);
        when(roleService.findByName(userInvitationDto.getRole())).thenReturn(TestDataFactory.createRole());

        authenticationService.inviteUser(userInvitationDto);

        verify(employeeService, times(1)).save(any(Employee.class));
        verify(emailService, times(1)).sendEmail(
                anyString(),
                anyString(),
                any(),
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void testInviteUser_whenUserAlreadyExists() {
        when(employeeService.findByEmail(userInvitationDto.getEmail())).thenReturn(employee);

        UserAlreadyExistsException thrown = assertThrows(UserAlreadyExistsException.class, () -> {
            authenticationService.inviteUser(userInvitationDto);
        });

        assertEquals(String.format("User with email %s already exists", userInvitationDto.getEmail()), thrown.getMessage());
    }

    @Test
    void testRegisterUser_whenTokenIsValid() {
        when(tokenService.findByToken(completeRegisterDto.getActivationCode())).thenReturn(token);
        when(employeeService.save(any(Employee.class))).thenReturn(employee);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(tokenService.save(any(Token.class))).thenReturn(token);

        authenticationService.registerUser(completeRegisterDto);

        verify(employeeService, times(1)).save(any(Employee.class));
        verify(tokenService, times(1)).save(any(Token.class));
    }

    @Test
    void testRegisterUser_whenTokenIsExpired() throws MessagingException {
        token.setExpiresAt(Instant.now().minusSeconds(600)); // Token'ı süresi dolmuş olarak ayarla
        when(tokenService.findByToken(completeRegisterDto.getActivationCode())).thenReturn(token);

        doNothing().when(emailService).sendEmail(
                anyString(), anyString(), any(), anyString(), anyString(), anyString()
        );

        ActivationTokenExpiredException thrown = assertThrows(ActivationTokenExpiredException.class, () -> {
            authenticationService.registerUser(completeRegisterDto);
        });

        assertEquals("Activation code has expired", thrown.getMessage());
        verify(emailService, times(1)).sendEmail(
                anyString(), anyString(), any(), anyString(), anyString(), anyString()
        );
    }

    
    @Test
    void testAuthenticate() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(employee);
        when(jwtService.generateToken(any(), any())).thenReturn("jwtToken");

        AuthenticationResponseDto response = authenticationService.authenticate(authenticationRequestDto);

        assertNotNull(response, "Response should not be null");
        assertEquals("jwtToken", response.getToken(), "JWT token does not match");
    }

    @Test
    void testActivateAccount_whenTokenIsValid() throws MessagingException {
        String tokenString = "validToken";
        when(tokenService.findByToken(tokenString)).thenReturn(token);
        when(employeeService.save(any(Employee.class))).thenReturn(employee);
        when(tokenService.save(any(Token.class))).thenReturn(token);

        String responseUrl = authenticationService.activateAccount(tokenString);

        assertNotNull(responseUrl);
        assertTrue(responseUrl.startsWith("http://localhost:8080/complete-registration"));
        verify(employeeService, times(1)).save(any(Employee.class));
        verify(tokenService, times(1)).save(any(Token.class));
    }

    @Test
    void testActivateAccount_whenTokenIsExpired() throws MessagingException {
        String tokenString = "expiredToken";
        token.setExpiresAt(Instant.now().minusSeconds(600));
        when(tokenService.findByToken(tokenString)).thenReturn(token);

        doNothing().when(emailService).sendEmail(
                anyString(), anyString(), any(), anyString(), anyString(), anyString()
        );

        ActivationTokenExpiredException thrown = assertThrows(ActivationTokenExpiredException.class, () -> {
            authenticationService.activateAccount(tokenString);
        });

        assertEquals("Activation token has expired. A new token has been sent to the same email address", thrown.getMessage());

        verify(emailService, times(1)).sendEmail(
                eq("expectedSenderEmail"),
                eq("expectedRecipientEmail"),
                any(),
                eq("expectedUrlWithActivationCode"),
                eq("210851"),
                eq("Account activation")
        );
    }

}


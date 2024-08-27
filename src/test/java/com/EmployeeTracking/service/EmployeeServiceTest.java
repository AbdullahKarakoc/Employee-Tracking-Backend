package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.domain.response.EmployeeResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.EmployeeRepository;
import com.EmployeeTracking.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private CompleteRegisterDto completeRegisterDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = TestDataFactory.createEmployee();
        completeRegisterDto = TestDataFactory.createCompleteRegisterDto();
    }

    @Test
    void testGetAllUsers() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeResponseDto> response = employeeService.getAllUsers();

        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
        assertEquals(1, response.size(), "Response size should be 1");
        EmployeeResponseDto dto = response.get(0);
        assertEquals(employee.getEmployeeId(), dto.getEmployeeId(), "Employee ID does not match");
        assertEquals(employee.getFirstname(), dto.getFirstname(), "Firstname does not match");
        assertEquals(employee.getLastname(), dto.getLastname(), "Lastname does not match");
        assertEquals(employee.getEmail(), dto.getEmail(), "Email does not match");

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        UUID id = employee.getEmployeeId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        EmployeeResponseDto response = employeeService.getUserById(id);

        assertNotNull(response, "Response should not be null");
        assertEquals(employee.getEmployeeId(), response.getEmployeeId(), "Employee ID does not match");
        assertEquals(employee.getFirstname(), response.getFirstname(), "Firstname does not match");
        assertEquals(employee.getLastname(), response.getLastname(), "Lastname does not match");
        assertEquals(employee.getEmail(), response.getEmail(), "Email does not match");

        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void testGetLoggedInUserDetails() {
        String email = employee.getEmail();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        EmployeeResponseDto response = employeeService.getLoggedInUserDetails();

        assertNotNull(response, "Response should not be null");
        assertEquals(email, response.getEmail(), "Employee email does not match");

        verify(employeeRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdateUser() {
        UUID id = employee.getEmployeeId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(employee);

        EmployeeResponseDto response = employeeService.updateUser(id, completeRegisterDto);

        assertNotNull(response, "Response should not be null");
        assertEquals(employee.getEmployeeId(), response.getEmployeeId(), "Employee ID does not match");
        assertEquals("John", response.getFirstname(), "Firstname does not match");
        assertEquals("Doe", response.getLastname(), "Lastname does not match");
        assertEquals("1234567890", response.getPhone(), "Phone number does not match");

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
    }

    @Test
    void testDeleteUser() {
        UUID id = employee.getEmployeeId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(employee);

        employeeService.deleteUser(id);

        assertTrue(employee.isDeleted(), "Employee should be marked as deleted");

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
    }

    @Test
    void testFindById_userNotFound() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> employeeService.findById(id), "Expected DataNotFoundException to be thrown");

        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllUsersWhenNoUsersExist() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeResponseDto> response = employeeService.getAllUsers();

        assertNotNull(response, "Response should not be null");
        assertTrue(response.isEmpty(), "Response should be empty");
    }

    @Test
    void testGetAllUsersWhenNoEmployeesExist() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeResponseDto> response = employeeService.getAllUsers();

        assertNotNull(response, "Response should not be null");
        assertTrue(response.isEmpty(), "Response should be an empty list");

        verify(employeeRepository, times(1)).findAll();
    }

}

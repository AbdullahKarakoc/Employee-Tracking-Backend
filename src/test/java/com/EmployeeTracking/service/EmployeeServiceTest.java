package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.model.Role;
import com.EmployeeTracking.domain.request.AuthenticationRequestDto;
import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.domain.response.EmployeeResponseDto;
import com.EmployeeTracking.domain.response.RoleResponseDto;
import com.EmployeeTracking.repository.EmployeeRepository;
import com.EmployeeTracking.exception.DataNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @DisplayName("Should return all employees")
    @Test
    void shouldReturnAllEmployees() {
        // Arrange
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstname("John");
        employee.setLastname("Doe");
        employee.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        employee.setEmail("john.doe@example.com");
        employee.setCreatedAt(Instant.now());
        employee.setUpdatedAt(Instant.now());
        Role role = new Role();
        role.setName("ROLE_USER");
        employee.setRoles(Collections.singletonList(role));

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setEmployeeId(employeeId);
        employeeResponseDto.setFirstname("John");
        employeeResponseDto.setLastname("Doe");
        employeeResponseDto.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        employeeResponseDto.setEmail("john.doe@example.com");
        employeeResponseDto.setCreatedAt(employee.getCreatedAt());
        employeeResponseDto.setUpdatedAt(employee.getUpdatedAt());
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setName("ROLE_USER");
        employeeResponseDto.setRoles(Collections.singletonList(roleResponseDto));

        // Stubbing EmployeeRepository.findAll
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));

        // Act
        List<EmployeeResponseDto> result = employeeService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        EmployeeResponseDto resultDto = result.get(0);
        assertEquals(employeeId, resultDto.getEmployeeId());
        assertEquals("John", resultDto.getFirstname());
        assertEquals("Doe", resultDto.getLastname());
        assertEquals(Instant.parse("1990-01-01T00:00:00Z"), resultDto.getDateOfBirth());
        assertEquals("john.doe@example.com", resultDto.getEmail());
        assertEquals(employee.getCreatedAt(), resultDto.getCreatedAt());
        assertEquals(employee.getUpdatedAt(), resultDto.getUpdatedAt());
        assertNotNull(resultDto.getRoles());
        assertEquals(1, resultDto.getRoles().size());
        assertEquals("ROLE_USER", resultDto.getRoles().get(0).getName());
    }

    @DisplayName("Should return empty list when no employees are found")
    @Test
    void shouldReturnEmptyListWhenNoEmployees() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EmployeeResponseDto> result = employeeService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @DisplayName("Should return employee by id")
    @Test
    void shouldReturnEmployeeById() {
        // Arrange
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstname("John");
        employee.setLastname("Doe");
        employee.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        employee.setEmail("john.doe@example.com");
        employee.setCreatedAt(Instant.now());
        employee.setUpdatedAt(Instant.now());
        Role role = new Role();
        role.setName("ROLE_USER");
        employee.setRoles(Collections.singletonList(role));

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setEmployeeId(employeeId);
        employeeResponseDto.setFirstname("John");
        employeeResponseDto.setLastname("Doe");
        employeeResponseDto.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        employeeResponseDto.setEmail("john.doe@example.com");
        employeeResponseDto.setCreatedAt(employee.getCreatedAt());
        employeeResponseDto.setUpdatedAt(employee.getUpdatedAt());
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setName("ROLE_USER");
        employeeResponseDto.setRoles(Collections.singletonList(roleResponseDto));

        // Stubbing EmployeeRepository.findById
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Act
        EmployeeResponseDto result = employeeService.getUserById(employeeId);

        // Assert
        assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals(Instant.parse("1990-01-01T00:00:00Z"), result.getDateOfBirth());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(employee.getCreatedAt(), result.getCreatedAt());
        assertEquals(employee.getUpdatedAt(), result.getUpdatedAt());
        assertNotNull(result.getRoles());
        assertEquals(1, result.getRoles().size());
        assertEquals("ROLE_USER", result.getRoles().get(0).getName());
    }

    @DisplayName("Should throw DataNotFoundException when employee not found by id")
    @Test
    void shouldThrowDataNotFoundExceptionWhenEmployeeNotFoundById() {
        // Arrange
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> employeeService.getUserById(employeeId));
    }

    @DisplayName("Should return logged-in user details")
    @Test
    void shouldReturnLoggedInUserDetails() {
        // Arrange
        String email = "john.doe@example.com";
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstname("John");
        employee.setLastname("Doe");
        employee.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        employee.setEmail(email);
        employee.setCreatedAt(Instant.now());
        employee.setUpdatedAt(Instant.now());
        Role role = new Role();
        role.setName("ROLE_USER");
        employee.setRoles(Collections.singletonList(role));

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setEmployeeId(employeeId);
        employeeResponseDto.setFirstname("John");
        employeeResponseDto.setLastname("Doe");
        employeeResponseDto.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        employeeResponseDto.setEmail(email);
        employeeResponseDto.setCreatedAt(employee.getCreatedAt());
        employeeResponseDto.setUpdatedAt(employee.getUpdatedAt());
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setName("ROLE_USER");
        employeeResponseDto.setRoles(Collections.singletonList(roleResponseDto));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Stubbing EmployeeRepository.findByEmail
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        // Act
        EmployeeResponseDto result = employeeService.getLoggedInUserDetails();

        // Assert
        assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals(Instant.parse("1990-01-01T00:00:00Z"), result.getDateOfBirth());
        assertEquals(email, result.getEmail());
        assertEquals(employee.getCreatedAt(), result.getCreatedAt());
        assertEquals(employee.getUpdatedAt(), result.getUpdatedAt());
        assertNotNull(result.getRoles());
        assertEquals(1, result.getRoles().size());
        assertEquals("ROLE_USER", result.getRoles().get(0).getName());
    }

    @DisplayName("Should update user details with password and other information")
    @Test
    void shouldUpdateUserWithDetails() {
        // Arrange
        UUID id = UUID.randomUUID();

        // CompleteRegisterDto with updated details
        CompleteRegisterDto completeRegisterDto = new CompleteRegisterDto();
        completeRegisterDto.setFirstName("Jane");
        completeRegisterDto.setLastName("Doe");
        completeRegisterDto.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        completeRegisterDto.setPassword("newPassword"); // Yeni şifre
        completeRegisterDto.setActivationCode("123456"); // Aktivasyon kodu

        // Existing employee details
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(id);
        existingEmployee.setFirstname("John");
        existingEmployee.setLastname("Doe");
        existingEmployee.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        existingEmployee.setEmail("john.doe@example.com");
        existingEmployee.setPassword("oldPassword"); // Eski şifre
        existingEmployee.setCreatedAt(Instant.now());
        existingEmployee.setUpdatedAt(Instant.now());

        // Updated employee details
        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeId(id);
        updatedEmployee.setFirstname(completeRegisterDto.getFirstName());
        updatedEmployee.setLastname(completeRegisterDto.getLastName());
        updatedEmployee.setDateOfBirth(completeRegisterDto.getDateOfBirth());
        updatedEmployee.setEmail(existingEmployee.getEmail()); // Email değişmedi
        updatedEmployee.setPassword(completeRegisterDto.getPassword()); // Güncellenmiş şifre
        updatedEmployee.setCreatedAt(existingEmployee.getCreatedAt());
        updatedEmployee.setUpdatedAt(Instant.now());

        // Expected EmployeeResponseDto
        EmployeeResponseDto expectedResponseDto = new EmployeeResponseDto();
        expectedResponseDto.setEmployeeId(id);
        expectedResponseDto.setFirstname(completeRegisterDto.getFirstName());
        expectedResponseDto.setLastname(completeRegisterDto.getLastName());
        expectedResponseDto.setDateOfBirth(completeRegisterDto.getDateOfBirth());
        expectedResponseDto.setEmail(existingEmployee.getEmail());
        expectedResponseDto.setCreatedAt(existingEmployee.getCreatedAt());
        expectedResponseDto.setUpdatedAt(updatedEmployee.getUpdatedAt());

        // Stubbing EmployeeRepository.findById
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        // Stubbing EmployeeRepository.save
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        EmployeeResponseDto result = employeeService.updateUser(id, completeRegisterDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getEmployeeId());
        assertEquals(completeRegisterDto.getFirstName(), result.getFirstname());
        assertEquals(completeRegisterDto.getLastName(), result.getLastname());
        assertEquals(completeRegisterDto.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(existingEmployee.getEmail(), result.getEmail());
        assertEquals(existingEmployee.getCreatedAt(), result.getCreatedAt());
        assertEquals(updatedEmployee.getUpdatedAt(), result.getUpdatedAt());
    }






    @DisplayName("Should throw DataNotFoundException when updating non-existing user")
    @Test
    void shouldThrowDataNotFoundExceptionWhenUpdatingNonExistingUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        CompleteRegisterDto dto = new CompleteRegisterDto();
        dto.setEmail("jane.doe@example.com");
        dto.setPassword("newpassword123");

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> employeeService.updateUser(id, dto));
    }

    @DisplayName("Should delete employee")
    @Test
    void shouldDeleteEmployee() {
        // Arrange
        UUID id = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setEmployeeId(id);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        // Act
        employeeService.deleteUser(id);

        // Assert
        verify(employeeRepository, times(1)).delete(employee);
    }

    @DisplayName("Should throw DataNotFoundException when deleting non-existing employee")
    @Test
    void shouldThrowDataNotFoundExceptionWhenDeletingNonExistingEmployee() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> employeeService.deleteUser(id));
    }
}

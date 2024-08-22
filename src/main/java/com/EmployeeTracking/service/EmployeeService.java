package com.EmployeeTracking.service;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.request.CompleteRegisterDto;
import com.EmployeeTracking.repository.EmployeeRepository;
import com.EmployeeTracking.domain.response.EmployeeResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public List<EmployeeResponseDto> getAllUsers() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return AppUtils.emptyList();
        }

        return ObjectMapperUtils.mapAll(employees, EmployeeResponseDto.class);
    }

    public EmployeeResponseDto getUserById(UUID id) {
        Employee employee = findById(id);
        return ObjectMapperUtils.map(employee, EmployeeResponseDto.class);

    }

    public EmployeeResponseDto getLoggedInUserDetails() {
        String username = getLoggedInUsername();
        Employee employee = findByEmail(username);

        return ObjectMapperUtils.map(employee, EmployeeResponseDto.class);
    }

    public EmployeeResponseDto updateUser(UUID id, CompleteRegisterDto completeRegisterDto) {
        Employee existingUser = findById(id);
        ObjectMapperUtils.map(completeRegisterDto, existingUser);
        Employee updatedUser = save(existingUser);

        EmployeeResponseDto userDto = ObjectMapperUtils.map(updatedUser, EmployeeResponseDto.class);
        return userDto;
    }

    public void deleteUser(UUID id) {
        Employee employee = findById(id);
        employee.setDeleted(true);
        save(employee);
    }

    public Employee findById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public Employee save(Employee employee) {
        return employeeRepository.saveAndFlush(employee);
    }

    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email).orElse(null);
    }

    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}

package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.request.AuthenticationRequestDto;
import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.repository.EmployeeRepository;
import com.EmployeeTracking.domain.response.EmployeeResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;


    public List<EmployeeResponseDto> getAllUsers() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeResponseDto.class))
                .collect(Collectors.toList());
    }

    public EmployeeResponseDto getUserById(UUID id) {
        Employee employee = findById(id);
        return modelMapper.map(employee, EmployeeResponseDto.class);

    }

    public EmployeeResponseDto updateUser(UUID id, AuthenticationRequestDto authenticationRequestDto) {
        Employee existingUser = findById(id);
        modelMapper.map(authenticationRequestDto, existingUser);
        Employee updatedUser = save(existingUser);

        EmployeeResponseDto userDto = modelMapper.map(updatedUser, EmployeeResponseDto.class);
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
        return employeeRepository.save(employee);
    }
}

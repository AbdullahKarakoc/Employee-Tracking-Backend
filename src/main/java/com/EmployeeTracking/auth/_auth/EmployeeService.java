package com.EmployeeTracking.auth._auth;

import com.EmployeeTracking.auth.user.Employee;
import com.EmployeeTracking.auth.user.EmployeeRepository;
import com.EmployeeTracking.auth.user.EmployeeResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private EmployeeService(
            EmployeeRepository employeeRepository,
            ModelMapper modelMapper){

        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


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

    public EmployeeResponseDto updateUser(UUID id, AuthenticationRequest authenticationRequest) {
        Employee existingUser = findById(id);
        modelMapper.map(authenticationRequest, existingUser);
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

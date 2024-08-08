package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Role;
import com.EmployeeTracking.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;


    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new IllegalStateException(String.format("Role %s was not initiated", name)));
    }

}

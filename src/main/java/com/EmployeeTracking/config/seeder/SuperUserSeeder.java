package com.EmployeeTracking.config.seeder;

import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.model.Role;
import com.EmployeeTracking.service.EmployeeService;
import com.EmployeeTracking.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SuperUserSeeder implements CommandLineRunner {

    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role superUserRole = roleService.findByName("SUPER_USER");

        if (employeeService.findByEmail("superuser@gmail.com") == null) {
            Employee superUser = new Employee();
            superUser.setFirstname("Super");
            superUser.setLastname("User");
            superUser.setEmail("superuser@gmail.com");
            superUser.setPassword(passwordEncoder.encode("SuperUser123"));
            superUser.setRoles(List.of(superUserRole));
            superUser.setEnabled(true);
            employeeService.save(superUser);
        }
    }
}

package com.EmployeeTracking.config.seeder;

import com.EmployeeTracking.domain.model.Role;
import com.EmployeeTracking.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExists("SUPER_USER");
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("USER");
    }

    private void createRoleIfNotExists(String roleName) {
        try {
            roleService.findByName(roleName);
        } catch (IllegalStateException e) {
            Role role = new Role();
            role.setName(roleName);
            roleService.save(role);
        }
    }
}

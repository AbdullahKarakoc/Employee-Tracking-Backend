package com.EmployeeTracking.config.auditing;

import com.EmployeeTracking.domain.model.Employee;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isNull(authentication) ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Employee userPrincipal = (Employee) authentication.getPrincipal();

        return Optional.ofNullable(userPrincipal.getUsername());
    }
}

package com.EmployeeTracking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE employee SET deleted = true WHERE userUUID=?")
@Where(clause = "deleted=false")
public class Employee implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private UUID employeeId;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String email;
    private String phone;
    private String password;
    private boolean accountLocked;
    private boolean enabled;
    private boolean deleted = Boolean.FALSE;

    @ManyToMany(fetch = EAGER) // associated objects are always loaded along with the parent object
    private List<Role> roles;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId", nullable = true)
    private Teams team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "performanceId", referencedColumnName = "performanceId")
    private Performances performance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectRolesId", nullable = true)
    private ProjectRoles projectRole;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskId", nullable = true)
    private Tasks task;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Comments> comment;



    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String getName() {
        return email;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }
}


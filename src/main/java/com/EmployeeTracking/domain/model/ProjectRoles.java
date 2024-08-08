package com.EmployeeTracking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project_roles")
@SQLDelete(sql = "UPDATE project_roles SET deleted = true WHERE projectRolesUUID=?")
@Where(clause = "deleted=false")
public class ProjectRoles {

    @Id
    @GeneratedValue
    private UUID projectRoleId;
    private String employeeRole;
    private String description;
    private boolean deleted = Boolean.FALSE;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId", nullable = true)
    private Projects project;

    @OneToMany(mappedBy = "projectRole", cascade = CascadeType.ALL)
    private List<Employee> employees;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}

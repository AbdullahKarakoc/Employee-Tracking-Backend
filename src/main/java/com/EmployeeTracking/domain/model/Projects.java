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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project")
@SQLDelete(sql = "UPDATE project SET deleted = true WHERE projectUUID=?")
@Where(clause = "deleted=false")
public class Projects {

    @Id
    @GeneratedValue
    private UUID projectId;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime deadline;
    private LocalDateTime finishDate;
    private boolean deleted = Boolean.FALSE;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="statusId", nullable=false)
    private Status status;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectRoles> projectRoles;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Tasks> tasks;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}

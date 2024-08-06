package com.EmployeeTracking.domain.model;

import com.EmployeeTracking.enums.TaskStatus;
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
@Table(name = "task")
@SQLDelete(sql = "UPDATE task SET deleted = true WHERE taskUUID=?")
@Where(clause = "deleted=false")
public class Tasks {

    @Id
    @GeneratedValue
    private UUID taskId;
    private String name;
    private TaskStatus status;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime deadline;
    private LocalDateTime finishDate;
    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId", nullable = true)
    private Projects project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comments> comment;


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

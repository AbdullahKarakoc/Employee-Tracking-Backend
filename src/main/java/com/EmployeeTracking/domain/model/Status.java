package com.EmployeeTracking.domain.model;

import com.EmployeeTracking.enums.ProcessStatus;
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
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "status")
@SQLDelete(sql = "UPDATE status SET deleted = true WHERE statusUUID=?")
@Where(clause = "deleted=false")
public class Status {

    @Id
    @GeneratedValue
    private UUID statusId;
    private ProcessStatus status;
    private String description;
    private boolean deleted = Boolean.FALSE;

    @OneToOne(mappedBy = "status", cascade = CascadeType.ALL)
    private Projects project;


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

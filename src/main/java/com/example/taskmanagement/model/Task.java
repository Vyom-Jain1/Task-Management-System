package com.example.taskmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_task_status", columnList = "status"),
        @Index(name = "idx_task_created_at", columnList = "created_at"),
        @Index(name = "idx_task_deadline", columnList = "deadline")
})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.TODO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "priority", nullable = false)
    private Integer priority = 3; // 1: High, 2: Medium, 3: Low

    @Column(name = "user_id", nullable = false)
    private Long userId = 1L; // Default user ID for single-user mode

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "estimated_hours")
    private Double estimatedHours;

    @Column(name = "actual_hours")
    private Double actualHours;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "attachment_filename")
    private String attachmentFilename;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getDaysUntilDeadline() {
        if (deadline == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(now, deadline);
    }

    public boolean isOverdue() {
        if (deadline == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(deadline);
    }

    public Long getDaysOverdue() {
        if (!isOverdue()) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(deadline, LocalDateTime.now());
    }
}
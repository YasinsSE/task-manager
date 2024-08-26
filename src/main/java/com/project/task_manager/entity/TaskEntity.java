package com.project.task_manager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String taskTitle;
    private String taskDescription;
    private LocalDateTime taskDueDate;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.PENDING; // Default value


    @ElementCollection
    private Set<Long> completedTaskIds = new HashSet<>();

    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED
    }

    @Data
    @NoArgsConstructor
    public static class CompletedTask {
        private Long userId;
        private Long taskId;

        public CompletedTask(Long userId, Long taskId) {
            this.userId = userId;
            this.taskId = taskId;
        }
    }
}

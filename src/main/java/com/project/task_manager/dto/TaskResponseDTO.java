package com.project.task_manager.dto;

import com.project.task_manager.entity.TaskEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDTO {
    private Long taskId;
    private String taskTitle;
    private String taskDescription;
    private LocalDateTime taskDueDate;
    private Long userId;
    private TaskEntity.TaskStatus taskStatus;
}
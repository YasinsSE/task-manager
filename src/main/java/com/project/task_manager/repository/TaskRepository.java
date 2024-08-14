package com.project.task_manager.repository;

import com.project.task_manager.entity.TaskEntity;
import com.project.task_manager.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    TaskEntity findByTaskId(Long taskId);
    List<TaskEntity> findByUserId(Long userId);
    List<TaskEntity> findByTaskStatus(TaskStatus taskStatus);  // PENDING, COMPLETED, IN_PROGRESS
    List<TaskEntity> findByTaskStatusAndUserId(TaskStatus taskStatus, Long userId);
    boolean existsByTaskTitleAndUserId(String taskTitle, Long userId);

}


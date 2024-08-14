package com.project.task_manager.repository;

import com.project.task_manager.entity.TaskEntity;
import com.project.task_manager.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    TaskEntity findByTaskId(Long taskId);
    List<TaskEntity> findByUser_UserId(Long userId);
    List<TaskEntity> findByUser_FullName(String userName);
    List<TaskEntity> findByTaskStatus(TaskStatus taskStatus);  // PENDING, COMPLETED, IN_PROGRESS
    List<TaskEntity> findByTaskStatusAndUser_UserId(TaskStatus taskStatus, Long userId);
}


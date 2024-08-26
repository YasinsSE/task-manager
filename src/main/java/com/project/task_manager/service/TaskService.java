package com.project.task_manager.service;

import com.project.task_manager.entity.TaskEntity;

import java.util.List;

public interface TaskService {
    public TaskEntity createTask(TaskEntity task);
    public TaskEntity updateTask(TaskEntity task);
    public String deleteTask(TaskEntity task);
    public String deleteTaskById(Long taskId);
    public TaskEntity getTask(Long taskId);
    public List<TaskEntity> getAllTasks();
    public TaskEntity assignTask(Long taskId, Long userId);
    public TaskEntity updateTaskStatus(Long taskId, TaskEntity.TaskStatus taskStatus);
    public List<TaskEntity> getCompletedTasks();
    public List<TaskEntity> getNonCompletedTasks();
}

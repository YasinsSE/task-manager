package com.project.task_manager.service;

import com.project.task_manager.dto.TaskRequestDTO;
import com.project.task_manager.dto.TaskResponseDTO;
import com.project.task_manager.entity.TaskEntity;

import java.util.List;

public interface TaskService {
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO);
    public TaskResponseDTO updateTask(TaskRequestDTO taskDTO);
    public String deleteTask(TaskRequestDTO taskDTO);
    public String deleteTaskById(Long taskId);
    public TaskResponseDTO getTask(Long taskId);
    public List<TaskResponseDTO> getAllTasks();
    public TaskResponseDTO assignTask(Long taskId, Long userId);
    public TaskResponseDTO updateTaskStatus(Long taskId, TaskEntity.TaskStatus taskStatus);
    public List<TaskResponseDTO> getCompletedTasks();
    public List<TaskResponseDTO> getNonCompletedTasks();
}

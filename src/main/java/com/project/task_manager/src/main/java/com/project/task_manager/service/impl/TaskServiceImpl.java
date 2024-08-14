package com.project.task_manager.service.impl;

import com.project.task_manager.entity.TaskEntity;
import com.project.task_manager.entity.TaskStatus;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.CustomNotFoundException;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TaskEntity createTask(TaskEntity task) {
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public TaskEntity updateTask(TaskEntity task) {
        TaskEntity existingTask = taskRepository.findById(task.getTaskId())
                .orElseThrow(() -> new CustomNotFoundException("Task not found"));

        if (task.getTaskTitle() != null && !task.getTaskTitle().isEmpty()) {
            existingTask.setTaskTitle(task.getTaskTitle());
        }
        if (task.getTaskDescription() != null && !task.getTaskDescription().isEmpty()) {
            existingTask.setTaskDescription(task.getTaskDescription());
        }
        if (task.getTaskDueDate() != null) {
            existingTask.setTaskDueDate(task.getTaskDueDate());
        }
        if (task.getTaskStatus() != null) {
            existingTask.setTaskStatus(task.getTaskStatus());
        }

        return taskRepository.save(existingTask);
    }
    @Override
    public String deleteTask(TaskEntity task) {
        taskRepository.delete(task);
        return "Task with Id " + task.getTaskId() + " Deleted Successfully";
    }

    @Override
    public String deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
        return "Task with Id " + taskId + " Deleted Successfully";
    }

    @Override
    public TaskEntity getTask(Long taskId) {
        return taskRepository.findByTaskId(taskId);
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public TaskEntity assignTask(Long taskId, Long userId) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        if (taskOptional.isPresent() && userOptional.isPresent()) {
            TaskEntity task = taskOptional.get();
            UserEntity user = userOptional.get();
            task.setUser(user);
            return taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task or User not found");
        }
    }


    @Override
    public TaskEntity updateTaskStatus(Long taskId, TaskStatus taskStatus) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            TaskEntity task = taskOptional.get();
            task.setTaskStatus(taskStatus);
            return taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task not found");
        }
    }
}

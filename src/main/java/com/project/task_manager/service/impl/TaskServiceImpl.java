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

import java.time.LocalDateTime;
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
        // Task title boşsa hata fırlat
        if (task.getTaskTitle() == null || task.getTaskTitle().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        // Task description boşsa hata fırlat
        if (task.getTaskDescription() == null || task.getTaskDescription().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }

        // Geçmiş bir tarih için taskDueDate ayarlanıyorsa hata fırlat
        if (task.getTaskDueDate() != null && task.getTaskDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Task due date cannot be in the past");
        }

        // taskStatus belirtilmemişse varsayılan olarak PENDING ata
        if (task.getTaskStatus() == null) {
            task.setTaskStatus(TaskStatus.PENDING);
        }

        // Eğer kullanıcı atanmışsa, kullanıcı mevcut mu kontrol et
        if (task.getUserId() != null) {
            Optional<UserEntity> userOptional = userRepository.findById(task.getUserId());
            if (!userOptional.isPresent()) {
                throw new IllegalArgumentException("Assigned user does not exist");
            }
        }

        // Aynı kullanıcı için aynı başlıklı bir görev var mı kontrol et
        if (task.getUserId() != null && taskRepository.existsByTaskTitleAndUserId(task.getTaskTitle(), task.getUserId())) {
            throw new IllegalArgumentException("Task with the same title already exists for the assigned user");
        }

        // Task'ı kaydet
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
    @Transactional
    public String deleteTaskById(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException("Task with ID " + taskId + " not found"));

        taskRepository.delete(task);
        return "Task with Id " + taskId + " deleted successfully.";
    }


    @Override
    public TaskEntity getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException("Task with ID " + taskId + " not found"));
    }


    @Override
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public TaskEntity assignTask(Long taskId, Long userId) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        if (taskOptional.isPresent() && userOptional.isPresent()) {
            TaskEntity task = taskOptional.get();
            UserEntity user = userOptional.get();

            task.setUserId(userId);

            // Kullanıcının taskId set'ine bu görevi ekliyoruz
            user.getTaskIds().add(taskId);

            taskRepository.save(task);
            userRepository.save(user);

            return task;
        } else {
            throw new IllegalArgumentException("Task or User not found");
        }
    }


    @Override
    @Transactional
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

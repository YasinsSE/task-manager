package com.project.task_manager.service.impl;

import com.project.task_manager.entity.CompletedTask;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private List<CompletedTask> completedTasksList = new ArrayList<>();


    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public TaskEntity createTask(TaskEntity task) {
        // Throw an error if the task title is empty
        if (task.getTaskTitle() == null || task.getTaskTitle().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        // Throw an error if the task description is empty
        if (task.getTaskDescription() == null || task.getTaskDescription().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }

        // Throw an error if setting taskDueDate to a past date
        if (task.getTaskDueDate() != null && task.getTaskDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Task due date cannot be in the past");
        }

        // If taskStatus is not specified, default to PENDING
        if (task.getTaskStatus() == null) {
            task.setTaskStatus(TaskStatus.PENDING);
        }

        // If user is assigned, check if user exists
        if (task.getUserId() != null) {
            boolean userExists = userRepository.existsById(task.getUserId());
            if (!userExists) {
                throw new CustomNotFoundException("Assigned user with Id " + task.getUserId() + " does not exist");
            }
        }

        // Check if there is a task with the same title for the same user
        if (task.getUserId() != null && taskRepository.existsByTaskTitleAndUserId(task.getTaskTitle(), task.getUserId())) {
            throw new IllegalArgumentException("Task with the same title already exists for the assigned user");
        }

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
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException("Task not found"));

        if (taskStatus == TaskStatus.COMPLETED) {
            // Remove task from user's task list
            UserEntity user = userRepository.findById(task.getUserId())
                    .orElseThrow(() -> new CustomNotFoundException("User not found"));
            user.getTaskIds().remove(taskId);

            // Save changes to user entity
            userRepository.save(user);

            // Add to completed tasks list
            completedTasksList.add(new CompletedTask(taskId, user.getUserId()));

            // Optionally save task if you need to keep track of completed tasks separately
            taskRepository.save(task);
        }

        task.setTaskStatus(taskStatus);
        return taskRepository.save(task);
    }


    @Override
    public List<TaskEntity> getCompletedTasks() {
        return taskRepository.findByTaskStatus(TaskStatus.COMPLETED);
    }

    @Override
    public List<TaskEntity> getNonCompletedTasks() {
        return taskRepository.findByTaskStatus(TaskStatus.PENDING);
    }

}

package com.project.task_manager.service.impl;

import com.project.task_manager.dto.TaskRequestDTO;
import com.project.task_manager.dto.TaskResponseDTO;
import com.project.task_manager.entity.TaskEntity;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.CustomNotFoundException;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final List<TaskEntity.CompletedTask> completedTasksList = new ArrayList<>();



    private TaskResponseDTO convertToDTO(TaskEntity taskEntity) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setTaskId(taskEntity.getTaskId());
        dto.setTaskTitle(taskEntity.getTaskTitle());
        dto.setTaskDescription(taskEntity.getTaskDescription());
        dto.setTaskDueDate(taskEntity.getTaskDueDate());
        dto.setUserId(taskEntity.getUserId());
        dto.setTaskStatus(taskEntity.getTaskStatus());
        return dto;
    }

    private TaskEntity convertToEntity(TaskRequestDTO dto) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTaskId(dto.getTaskId());
        taskEntity.setTaskTitle(dto.getTaskTitle());
        taskEntity.setTaskDescription(dto.getTaskDescription());
        taskEntity.setTaskDueDate(dto.getTaskDueDate());
        taskEntity.setUserId(dto.getUserId());
        taskEntity.setTaskStatus(dto.getTaskStatus());
        return taskEntity;
    }


    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO) {
        TaskEntity task = convertToEntity(taskDTO);

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
            task.setTaskStatus(TaskEntity.TaskStatus.PENDING);
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

        return convertToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(TaskRequestDTO taskDTO) {
        TaskEntity existingTask = taskRepository.findById(taskDTO.getTaskId())
                .orElseThrow(() -> new CustomNotFoundException("Task not found"));

        if (taskDTO.getTaskTitle() != null && !taskDTO.getTaskTitle().isEmpty()) {
            existingTask.setTaskTitle(taskDTO.getTaskTitle());
        }
        if (taskDTO.getTaskDescription() != null && !taskDTO.getTaskDescription().isEmpty()) {
            existingTask.setTaskDescription(taskDTO.getTaskDescription());
        }
        if (taskDTO.getTaskDueDate() != null) {
            existingTask.setTaskDueDate(taskDTO.getTaskDueDate());
        }
        if (taskDTO.getTaskStatus() != null) {
            existingTask.setTaskStatus(taskDTO.getTaskStatus());
        }

        return convertToDTO(taskRepository.save(existingTask));
    }

    @Override
    public String deleteTask(TaskRequestDTO taskDTO) {
        TaskEntity task = convertToEntity(taskDTO);
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
    public TaskResponseDTO getTask(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException("Task with ID " + taskId + " not found"));
        return convertToDTO(task);
    }


    @Override
    public List<TaskResponseDTO> getAllTasks() {
        List<TaskEntity> tasks = taskRepository.findAll();
        List<TaskResponseDTO> taskDTOs = new ArrayList<>();
        for (TaskEntity task : tasks) {
            taskDTOs.add(convertToDTO(task));
        }
        return taskDTOs;
    }

    @Override
    @Transactional
    public TaskResponseDTO assignTask(Long taskId, Long userId) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        if (taskOptional.isPresent() && userOptional.isPresent()) {
            TaskEntity task = taskOptional.get();
            UserEntity user = userOptional.get();

            task.setUserId(userId);

            user.getTaskIds().add(taskId);

            taskRepository.save(task);
            userRepository.save(user);

            return convertToDTO(task);
        } else {
            throw new IllegalArgumentException("Task or User not found");
        }
    }


    @Override
    @Transactional
    public TaskResponseDTO updateTaskStatus(Long taskId, TaskEntity.TaskStatus taskStatus) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException("Task not found"));

        if (taskStatus == TaskEntity.TaskStatus.COMPLETED) {
            // Remove task from user's task list
            UserEntity user = userRepository.findById(task.getUserId())
                    .orElseThrow(() -> new CustomNotFoundException("User not found"));
            user.getTaskIds().remove(taskId);

            // Save changes to user entity
            userRepository.save(user);

            // Add to completed tasks list
            completedTasksList.add(new TaskEntity.CompletedTask(taskId, user.getUserId()));

            // Optionally save task if you need to keep track of completed tasks separately
            taskRepository.save(task);
        }

        task.setTaskStatus(taskStatus);
        return convertToDTO(taskRepository.save(task));
    }


    @Override
    public List<TaskResponseDTO> getCompletedTasks() {
        List<TaskEntity> tasks = taskRepository.findByTaskStatus(TaskEntity.TaskStatus.COMPLETED);
        List<TaskResponseDTO> taskDTOs = new ArrayList<>();
        for (TaskEntity task : tasks) {
            taskDTOs.add(convertToDTO(task));
        }
        return taskDTOs;
    }

    @Override
    public List<TaskResponseDTO> getNonCompletedTasks() {
        List<TaskEntity> tasks = taskRepository.findByTaskStatus(TaskEntity.TaskStatus.PENDING);
        List<TaskResponseDTO> taskDTOs = new ArrayList<>();
        for (TaskEntity task : tasks) {
            taskDTOs.add(convertToDTO(task));
        }
        return taskDTOs;
    }

}

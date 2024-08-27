package com.project.task_manager.controller;

import com.project.task_manager.dto.TaskRequestDTO;
import com.project.task_manager.dto.TaskResponseDTO;
import com.project.task_manager.entity.TaskEntity;
import com.project.task_manager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskController {

    private final TaskService taskService;

    // Create a new task
    @PostMapping("/create-task")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskDTO) {
        TaskResponseDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }


    // Update a task
    @PutMapping("/update-task")
    public ResponseEntity<TaskResponseDTO> updateTask(@RequestBody TaskRequestDTO taskDTO) {
        TaskResponseDTO updatedTask = taskService.updateTask(taskDTO);
        return ResponseEntity.ok(updatedTask);
    }


    // Delete a task by TaskEntity (Admin only)
    @DeleteMapping("/delete-task")
    public ResponseEntity<Void> deleteTask(@RequestBody TaskRequestDTO taskDTO) {
        String result = taskService.deleteTask(taskDTO);
        if (result.contains("Deleted Successfully")) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Delete a task by ID (Admin only)
    @DeleteMapping("/delete-task/by-id")
    public ResponseEntity<Void> deleteTaskById(@RequestParam Long taskId) {
        String result = taskService.deleteTaskById(taskId);
        if (result.contains("Deleted Successfully")) {
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }


    // Get all tasks
    @GetMapping("/list-tasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }


    // Get completed tasks
    @GetMapping("/completed-tasks")
    public ResponseEntity<List<TaskResponseDTO>> getCompletedTasks() {
        List<TaskResponseDTO> completedTasks = taskService.getCompletedTasks();
        return ResponseEntity.ok(completedTasks);
    }


    @GetMapping("/non-completed-tasks")
    public ResponseEntity<List<TaskResponseDTO>> getNonCompletedTasks() {
        List<TaskResponseDTO> nonCompletedTasks = taskService.getNonCompletedTasks();
        return ResponseEntity.ok(nonCompletedTasks);
    }


    // Get a single task by ID
    @GetMapping("/list-task")
    public ResponseEntity<TaskResponseDTO> getTask(@RequestParam Long taskId) {
        TaskResponseDTO task = taskService.getTask(taskId);
        return ResponseEntity.ok(task);
    }


    // Assign a task to a user (Admin only)
    @PutMapping("/assign-task")
    public ResponseEntity<TaskResponseDTO> assignTask(@RequestParam Long taskId, @RequestParam Long userId) {
        TaskResponseDTO assignedTask = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(assignedTask);
    }


    // Update task status
    @PutMapping("/update-task-status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(@RequestParam Long taskId, @RequestBody TaskEntity.TaskStatus taskStatus) {
        TaskResponseDTO updatedTask = taskService.updateTaskStatus(taskId, taskStatus);
        return ResponseEntity.ok(updatedTask);
    }
}

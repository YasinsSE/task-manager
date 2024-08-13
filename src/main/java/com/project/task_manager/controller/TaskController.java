package com.project.task_manager.controller;

import com.project.task_manager.entity.TaskEntity;
import com.project.task_manager.entity.TaskStatus;
import com.project.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create a new task
    @PostMapping("/create-task")
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity task) {
        TaskEntity createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    // Update a task
    @PutMapping("/update-task")
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity task) {
        TaskEntity updatedTask = taskService.updateTask(task);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete a task by TaskEntity (Admin only)
    @DeleteMapping("/delete-task")
    public ResponseEntity<Void> deleteTask(@RequestBody TaskEntity task) {
        String result = taskService.deleteTask(task);
        if (result.contains("Deleted Successfully")) {
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
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
    public ResponseEntity<List<TaskEntity>> getAllTasks() {
        List<TaskEntity> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // Get a single task by ID
    @GetMapping("/list-task")
    public ResponseEntity<TaskEntity> getTask(@RequestParam Long taskId) {
        TaskEntity task = taskService.getTask(taskId);
        return ResponseEntity.ok(task);
    }

    // Assign a task to a user (Admin only)
    @PostMapping("/assign-task")
    public ResponseEntity<TaskEntity> assignTask(@RequestParam Long taskId, @RequestParam Long userId) {
        TaskEntity assignedTask = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(assignedTask);
    }

    // Update task status (Users)
    @PutMapping("/update-task-status")
    public ResponseEntity<TaskEntity> updateTaskStatus(@RequestParam Long taskId, @RequestBody TaskStatus taskStatus) {
        TaskEntity updatedTask = taskService.updateTaskStatus(taskId, taskStatus);
        return ResponseEntity.ok(updatedTask);
    }
}

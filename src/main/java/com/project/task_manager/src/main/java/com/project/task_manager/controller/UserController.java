package com.project.task_manager.controller;


import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-api")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user (Admin only)
    @PostMapping("/create-user")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        UserEntity createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Update a user (Admin only)
    @PutMapping("/update-user")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user) {
        UserEntity updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user by ID (Admin only)
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        String result = userService.deleteUser(userId);
        if (result.contains("Deleted Successfully")) {
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    // Get a user by ID
    @GetMapping("/list-user")
    public ResponseEntity<UserEntity> getUserById(@RequestParam Long userId) {
        UserEntity user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Get all users
    @GetMapping("/list-users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Update a user's role (Admin only)
    @PutMapping("/update-user-role")
    public ResponseEntity<UserEntity> updateUserRole(@RequestParam Long userId, @RequestParam String role) {
        UserEntity updatedUser = userService.updateUserRole(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

}
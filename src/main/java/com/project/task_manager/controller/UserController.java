package com.project.task_manager.controller;


import com.project.task_manager.dto.UserRequestDTO;
import com.project.task_manager.dto.UserResponseDTO;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.CustomNotFoundException;
import com.project.task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserService userService;

    // Create a new user (Admin only)
    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userDTO) {
        UserResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }


    // Update a user (Admin only)
    @PutMapping("/update-user")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userDTO) {
        UserResponseDTO updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    // Delete a user by ID (Admin only)
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        try {
            String result = userService.deleteUser(userId);
            return ResponseEntity.ok(result);  // 200 OK with the message from the service
        } catch (CustomNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());  // 404 Not Found if user doesn't exist
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user.");
        }
    }


    // Get a user by ID
    @GetMapping("/list-user")
    public ResponseEntity<UserResponseDTO> getUserById(@RequestParam Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }


    // Get all users
    @GetMapping("/list-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    // Update a user's role (Admin only)
    @PutMapping("/update-user-role")
    public ResponseEntity<UserResponseDTO> updateUserRole(@RequestParam Long userId, @RequestParam String role) {
        UserResponseDTO updatedUser = userService.updateUserRole(userId, role);
        return ResponseEntity.ok(updatedUser);
    }
}

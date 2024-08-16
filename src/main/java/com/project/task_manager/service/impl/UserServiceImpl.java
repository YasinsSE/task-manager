package com.project.task_manager.service.impl;

import com.project.task_manager.entity.TaskEntity;
import com.project.task_manager.entity.TaskStatus;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.CustomNotFoundException;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    @Transactional
    public UserEntity createUser(UserEntity user) {

        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is mandatory");
        }

        if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is mandatory");
        }

        if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is mandatory");
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        return userRepository.save(user);
    }



    @Override
    @Transactional
    public UserEntity updateUser(UserEntity user) {
        UserEntity existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

        if (user.getFullName() != null && !user.getFullName().isEmpty()) {
            existingUser.setFullName(user.getFullName());
        }
        if (user.getUserEmail() != null && !user.getUserEmail().isEmpty()) {
            existingUser.setUserEmail(user.getUserEmail());
        }
        if (user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(user.getUserPassword());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public UserEntity updateUserTasks(Long userId, Long taskId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

        user.getTaskIds().remove(taskId);

        return userRepository.save(user);
    }


    @Override
    @Transactional
    public String deleteUser(Long userId) {
        UserEntity user = getUserById(userId);

        // Check if the user has any existing tasks
        if (user.getTaskIds() != null && !user.getTaskIds().isEmpty()) {
            throw new IllegalStateException("User with Id " + userId + " cannot be deleted as they have assigned tasks.");
        }

        try {
            userRepository.delete(user);
        } catch (Exception e) {
            System.err.println("Error occurred while deleting user with Id " + userId + ": " + e.getMessage());
            throw new RuntimeException("Failed to delete user with Id " + userId, e);
        }

        return "User with Id " + userId + " has been deleted successfully.";
    }



    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserEntity updateUserRole(Long userId, String role) {
        UserEntity user = getUserById(userId);
        user.setRole(role);
        return userRepository.save(user);
    }

}



package com.project.task_manager.service;

import com.project.task_manager.entity.UserEntity;

import java.util.List;

public interface UserService {
    public UserEntity createUser(UserEntity user);
    public UserEntity updateUser(UserEntity user);
    public String deleteUser(Long userId);
    public UserEntity getUserById(Long userId);
    public List<UserEntity> getAllUsers();
    public UserEntity updateUserRole(Long userId, String role);
    public UserEntity updateUserTasks(Long userId, Long taskId);
}

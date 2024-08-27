package com.project.task_manager.service;

import com.project.task_manager.dto.UserRequestDTO;
import com.project.task_manager.dto.UserResponseDTO;
import com.project.task_manager.entity.UserEntity;

import java.util.List;

public interface UserService {
    public UserResponseDTO createUser(UserRequestDTO userDTO);
    public UserResponseDTO updateUser(UserRequestDTO userDTO);
    public String deleteUser(Long userId);
    public UserResponseDTO getUserById(Long userId);
    public List<UserResponseDTO> getAllUsers();
    public UserResponseDTO updateUserRole(Long userId, String role);
    public UserResponseDTO updateUserTasks(Long userId, Long taskId);
}

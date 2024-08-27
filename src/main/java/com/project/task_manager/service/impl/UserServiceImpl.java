package com.project.task_manager.service.impl;

import com.project.task_manager.dto.UserRequestDTO;
import com.project.task_manager.dto.UserResponseDTO;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.CustomNotFoundException;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    private UserResponseDTO convertToDTO(UserEntity userEntity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(userEntity.getUserId());
        dto.setFullName(userEntity.getFullName());
        dto.setUserEmail(userEntity.getUserEmail());
        dto.setRole(userEntity.getRole());
        dto.setTaskIds(userEntity.getTaskIds());
        return dto;
    }

    private UserEntity convertToEntity(UserRequestDTO dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(dto.getUserId());
        userEntity.setFullName(dto.getFullName());
        userEntity.setUserEmail(dto.getUserEmail());
        userEntity.setUserPassword(dto.getUserPassword());
        userEntity.setRole(dto.getRole());
        userEntity.setTaskIds(dto.getTaskIds() != null ? new HashSet<>(dto.getTaskIds()) : new HashSet<>());
        return userEntity;
    }


    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {

        if (userDTO.getFullName() == null || userDTO.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is mandatory");
        }

        if (userDTO.getUserEmail() == null || userDTO.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is mandatory");
        }

        if (userDTO.getUserPassword() == null || userDTO.getUserPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is mandatory");
        }

        if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
            userDTO.setRole("USER");
        }

        if (userRepository.existsByUserEmail(userDTO.getUserEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserEntity user = convertToEntity(userDTO);
        return convertToDTO(userRepository.save(user));
    }




    @Override
    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO userDTO) {
        UserEntity existingUser = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

        if (userDTO.getFullName() != null && !userDTO.getFullName().isEmpty()) {
            existingUser.setFullName(userDTO.getFullName());
        }
        if (userDTO.getUserEmail() != null && !userDTO.getUserEmail().isEmpty()) {
            existingUser.setUserEmail(userDTO.getUserEmail());
        }
        if (userDTO.getUserPassword() != null && !userDTO.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(userDTO.getUserPassword());
        }

        return convertToDTO(userRepository.save(existingUser));
    }

    @Override
    @Transactional
    public UserResponseDTO updateUserTasks(Long userId, Long taskId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

        user.getTaskIds().remove(taskId);

        return convertToDTO(userRepository.save(user));
    }


    @Override
    @Transactional
    public String deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

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
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));
        return convertToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public UserResponseDTO updateUserRole(Long userId, String role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));
        user.setRole(role);
        return convertToDTO(userRepository.save(user));
    }

}



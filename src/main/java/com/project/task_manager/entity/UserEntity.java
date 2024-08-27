package com.project.task_manager.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String fullName;
    private String userEmail;
    private String userPassword;
    private String role;

    @ElementCollection
    private Set<Long> taskIds = new HashSet<>();
}
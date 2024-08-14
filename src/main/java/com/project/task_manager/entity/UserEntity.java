package com.project.task_manager.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
// @Table(name="task_management")
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


// Constructors

    public UserEntity(Long userId, String fullName, String userEmail, String userPassword, String role, Set<Long> taskIds) {
        this.userId = userId;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.role = role;
        this.taskIds = taskIds;
    }

    public UserEntity() {
    }

    // Getter and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Set<Long> taskIds) {
        this.taskIds = taskIds;
    }
}

package com.project.task_manager.entity;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "user")
    private Set<TaskEntity> tasks;

    // Constructors

    public UserEntity(Long userId, String fullName, String userEmail, String userPassword, String role, Set<TaskEntity> tasks) {
        this.userId = userId;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.role = role;
        this.tasks = tasks;
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

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}

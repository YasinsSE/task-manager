package com.project.task_manager.repository;

import com.project.task_manager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByFullName(String userName);
    UserEntity findByUserEmail(String userEmail);
    UserEntity findByUserId(Long userId);

    @Query("SELECT u.userId FROM UserEntity u WHERE u.fullName IS NULL AND u.userEmail IS NULL AND u.userPassword IS NULL")
    List<Long> findEmptyUserIds();


    @Query("SELECT u FROM UserEntity u WHERE u.tasks IS EMPTY")
    List<UserEntity> findUsersWithNoTask();
}


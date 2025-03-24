package com.example.ballup_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ballup_backend.entity.NotificationEntity;
import com.example.ballup_backend.entity.UserEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("SELECT n FROM NotificationEntity n WHERE n.forUser = :user ORDER BY n.createdAt DESC")
    List<NotificationEntity> findAllByUser(@Param("user") UserEntity user);
    
}

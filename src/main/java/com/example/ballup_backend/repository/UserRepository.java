package com.example.ballup_backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {  
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
}

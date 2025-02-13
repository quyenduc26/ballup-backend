package com.example.ballup_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.exception.BaseException;
import com.example.ballup_backend.exception.ErrorCodeEnum;
import com.example.ballup_backend.projection.user.UserDetailProjection;
import com.example.ballup_backend.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; 

    public UserDetailProjection getAuthenticatedUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return UserDetailProjection.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }
}

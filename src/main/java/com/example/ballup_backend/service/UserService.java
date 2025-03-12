package com.example.ballup_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.user.UserInfoUpdateRequest;
import com.example.ballup_backend.dto.res.user.UserInfoResponse;
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

    public UserInfoResponse getUserInfo(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .filter(u -> u.getRole() == UserEntity.Role.USER) 
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return UserInfoResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .number(user.getPhone())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .avatar(user.getAvatar())
            .weight(user.getWeight())
            .height(user.getHeight())
            .build();
    }


    public void updateUserInfo(Long userId, UserInfoUpdateRequest request) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        if (request.getWeight() != null) user.setWeight(request.getWeight());
        if (request.getHeight() != null) user.setHeight(request.getHeight());

        userRepository.save(user);
    }
}

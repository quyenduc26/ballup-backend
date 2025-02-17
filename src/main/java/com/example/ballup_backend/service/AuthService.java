package com.example.ballup_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.authRequest.LoginRequest;
import com.example.ballup_backend.dto.req.authRequest.RegisterRequest;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.UserEntity.Role;
import com.example.ballup_backend.exception.BaseException;
import com.example.ballup_backend.exception.ErrorCodeEnum;
import com.example.ballup_backend.repository.UserRepository;
import com.example.ballup_backend.util.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registerUser(RegisterRequest request) {
        if (userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isPresent()) {
            throw new BaseException(ErrorCodeEnum.USER_ALREADY_EXITS, HttpStatus.CONFLICT);
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail()) 
                .password(passwordEncoder.encode(request.getPassword())) 
                .role(request.getRole().equals("user") ? Role.USER : Role.OWNER)
                .build();
        return userRepository.save(user);
    }

    public String loginUser(LoginRequest request) {
        UserEntity user = userRepository.findByUsernameOrEmail(request.getEmailOrUsername(),request.getEmailOrUsername() )
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND ));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(ErrorCodeEnum.INCORRECT_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        return jwtUtil.generateToken(user.getUsername());
    }
}
    
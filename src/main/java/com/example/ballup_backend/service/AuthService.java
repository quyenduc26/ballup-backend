package com.example.ballup_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.auth.LoginRequest;
import com.example.ballup_backend.dto.req.auth.RegisterRequest;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.UserEntity.Role;
import com.example.ballup_backend.exception.BaseException;
import com.example.ballup_backend.exception.ErrorCodeEnum;
import com.example.ballup_backend.projection.user.UserGoogleData;
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

    public String saveOrUpdateGoogleUser(UserGoogleData userGoogleData) {
        Optional<UserEntity> existingUserOpt = userRepository.findByEmail(userGoogleData.getEmail());
    
        UserEntity user;
        
        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
            user.setGoogleId(userGoogleData.getGoogleId());
            user.setUsername(userGoogleData.getUsername());
            user.setFirstName(userGoogleData.getFirstname());
            user.setLastName(userGoogleData.getLastname());
            user.setAvatar(userGoogleData.getAvatar());
        } else {
            user = UserEntity.builder()
                    .googleId(userGoogleData.getGoogleId())
                    .email(userGoogleData.getEmail())
                    .username(userGoogleData.getUsername())
                    .firstName(userGoogleData.getFirstname())
                    .lastName(userGoogleData.getLastname())
                    .avatar(userGoogleData.getAvatar())
                    .role(userGoogleData.getRole())
                    .build();
        }
        user = userRepository.save(user);
        return jwtUtil.generateToken(user.getUsername());
    }
    
    
}
    
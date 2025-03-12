package com.example.ballup_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.user.UserInfoUpdateRequest;
import com.example.ballup_backend.dto.res.user.UserInfoResponse;
import com.example.ballup_backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/{userId}/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> UpdateInfo(@PathVariable Long userId, @RequestBody UserInfoUpdateRequest userInfoUpdateRequest) {
        userService.updateUserInfo(userId, userInfoUpdateRequest);
        return ResponseEntity.ok("User information updated successfully!");
    }

    // @PatchMapping
    // public ResponseEntity<String> changePassword() {
    //     return ResponseEntity.ok("Hello admin check");
    // }

    // @DeleteMapping
    // public ResponseEntity<String> deleteAccount() {
    //     return ResponseEntity.ok("Hello admin check");
    // }
    
}

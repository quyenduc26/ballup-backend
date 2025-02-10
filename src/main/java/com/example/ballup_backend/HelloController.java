package com.example.ballup_backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public List<User> sayHello() {
        return userService.getAllUsers();
    }
}

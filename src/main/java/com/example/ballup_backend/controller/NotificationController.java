package com.example.ballup_backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
    
    //app/sendMessage
    @MessageMapping("/sendMessage")
    
    @SendTo("/topic/notification")
    public String sendMessage(String message){
        System.out.println(message);
        return message;
    }
}

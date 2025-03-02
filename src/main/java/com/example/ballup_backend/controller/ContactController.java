package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.ballup_backend.dto.req.contact.CreateFeedbackRequest;
import com.example.ballup_backend.dto.res.contact.FeedbackResponse;
import com.example.ballup_backend.service.FeedbackService;

@RestController
@RequestMapping("contact")
public class ContactController {
    
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<String> createFeedback(@RequestBody CreateFeedbackRequest request) {
        feedbackService.createFeedback(request);
        return ResponseEntity.ok("Form is submitted successfully");
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        List<FeedbackResponse> feedbacks = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbacks);
    }
}

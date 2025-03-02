package com.example.ballup_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.contact.CreateFeedbackRequest;
import com.example.ballup_backend.dto.res.contact.FeedbackResponse;
import com.example.ballup_backend.entity.FeedbackEntity;
import com.example.ballup_backend.repository.FeedbackRepository;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

     public FeedbackEntity createFeedback(CreateFeedbackRequest request) {
        FeedbackEntity feedback = FeedbackEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .content(request.getContent())
                .build();
        return feedbackRepository.save(feedback);
    }

    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAll().stream()
                .map(feedback -> FeedbackResponse.builder()
                        .name(feedback.getName())
                        .email(feedback.getEmail())
                        .content(feedback.getContent())
                        .build())
                .collect(Collectors.toList());
    }
    
}

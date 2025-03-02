package com.example.ballup_backend.dto.res.contact;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FeedbackResponse {
    private String name;
    private String email;
    private String content;
}

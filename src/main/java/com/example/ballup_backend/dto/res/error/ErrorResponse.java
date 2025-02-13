package com.example.ballup_backend.dto.res.error;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private  int code;
    private String message;

    public Map<String, Object> toMap() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", timestamp);
        response.put("status", status);
        response.put("error", error);
        response.put("code", code);
        response.put("message", message);
        return response;
    }
}

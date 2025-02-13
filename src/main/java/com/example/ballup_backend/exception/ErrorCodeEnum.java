package com.example.ballup_backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeEnum {
    USER_NOT_FOUND(1001, "User not found"),
    INVALID_REQUEST(1002, "Invalid request"),
    INTERNAL_ERROR(1003, "Internal server error"),
    DATA_NOT_FOUND(1004, "The requested URL was not found on this server."),
    USER_ALREADY_EXITS(1005, "User already exits"),
    INCORRECT_PASSWORD(1006, "Incorrect password");

    private final int code;
    private final String message;
}


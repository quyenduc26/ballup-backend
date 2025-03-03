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
    INCORRECT_PASSWORD(1006, "Incorrect password"),
    INVALID_EMAIL_OR_USERNAME(1007, "Invalid email or username"),
    USER_NOT_IN_TEAM(1008, "Member does not belong to the specified team"),
    FORBIDDEN(1009, "Don't have permission"),
    CANNOT_KICK_SELF(1009, "Can not kick your self"),
    MEMBER_NOT_FOUND(1010, "Team member not found")

    ;


    private final int code;
    private final String message;
}


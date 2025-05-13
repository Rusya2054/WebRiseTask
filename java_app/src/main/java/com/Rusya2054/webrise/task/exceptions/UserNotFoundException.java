package com.Rusya2054.webrise.task.exceptions;

import java.io.IOException;

public class UserNotFoundException extends IOException {
    private final Long userId;
    public UserNotFoundException(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return String.format("User with id: '%s' is not found", this.userId);
    }
}

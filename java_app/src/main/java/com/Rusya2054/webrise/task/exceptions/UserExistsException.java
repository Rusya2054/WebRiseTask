package com.Rusya2054.webrise.task.exceptions;

import java.io.IOException;

public class UserExistsException extends IOException {
    private final String username;
    public UserExistsException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return String.format("User with username: '%s' is exists", this.username);
    }
}

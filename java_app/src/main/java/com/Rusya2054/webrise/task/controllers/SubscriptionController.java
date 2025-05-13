package com.Rusya2054.webrise.task.controllers;

import com.Rusya2054.webrise.task.exceptions.UserExistsException;
import com.Rusya2054.webrise.task.exceptions.UserNotFoundException;
import com.Rusya2054.webrise.task.exceptions.ValidationInputDataException;
import com.Rusya2054.webrise.task.models.User;
import com.Rusya2054.webrise.task.services.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("subscriptions")
public class SubscriptionController {

    private final UserService userService;
    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("top")
    public ResponseEntity<Map<String, Object>> getUser() {
        try {
            return ResponseEntity.ok(
                    Map.of("subscriptions", userService.getTopSubscriptions(3L)));
        } catch (DataAccessException | PersistenceException ex) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

}

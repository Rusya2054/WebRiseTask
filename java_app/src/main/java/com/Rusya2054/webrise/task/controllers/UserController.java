package com.Rusya2054.webrise.task.controllers;

import com.Rusya2054.webrise.task.exceptions.SubscriptionNotFoundException;
import com.Rusya2054.webrise.task.exceptions.UserExistsException;
import com.Rusya2054.webrise.task.exceptions.UserNotFoundException;
import com.Rusya2054.webrise.task.exceptions.ValidationInputDataException;
import com.Rusya2054.webrise.task.models.Subscription;
import com.Rusya2054.webrise.task.models.User;
import com.Rusya2054.webrise.task.services.SubscriptionService;
import com.Rusya2054.webrise.task.services.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, String>> getUser(@PathVariable("id") Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(
                    Map.of("userId", user.getId().toString(), "username", user.getUsername()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (DataAccessException | PersistenceException ex) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody(required = true) UserRequestBody userRequestBody){
        try {
            userRequestBody.validate();
            if(userService.isExists(userRequestBody.username.trim())) {
                throw new UserExistsException(userRequestBody.username.trim());
            }
            userService.createUser(userRequestBody.username.trim());
            return ResponseEntity.ok().body(Map.of("message", "the operation was completed successfully"));
        } catch (ValidationInputDataException | UserExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (DataAccessException | PersistenceException ex) {
            ex.getStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable("id") Long userId, @RequestBody(required = true) UserRequestBody userRequestBody) {
        try {
            userRequestBody.validate();
            User user = userService.updateUser(userId, userRequestBody.username.trim());
            return ResponseEntity.ok(
                    Map.of("userId", user.getId().toString(), "username", user.getUsername()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (ValidationInputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (DataAccessException | PersistenceException ex) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

    private static class UserRequestBody{
        @JsonProperty("username")
        private String username;

        private void validate() throws ValidationInputDataException{
            if (username == null || username.trim().isEmpty()) {
                throw new ValidationInputDataException("username", "username is null or empty");
            }
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok().body(Map.of("message", "the operation was completed successfully"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (DataAccessException | PersistenceException ex) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

    @GetMapping("{id}/subscriptions")
    public ResponseEntity<Map<String, Object>> getUsersSubscriptions(@PathVariable("id") Long userId) {
        try {
            Set<Subscription> subscriptions = userService.getUsersSubscriptions(userId);

            return ResponseEntity.ok(
                    Map.of("subscriptions", subscriptions));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (DataAccessException | PersistenceException ex) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

    @PostMapping("{id}/subscriptions")
    public ResponseEntity<Map<String, String>> addSubscription(@PathVariable("id") Long userId, @RequestBody(required = true) SubscriptionRequestBody subscriptionRequestBody) {
        try {
            subscriptionRequestBody.validate();

            Subscription subscription = userService.updateUsersSubscription(userId, subscriptionRequestBody.name.trim());
            return ResponseEntity.ok(
                    Map.of("userId", userId.toString(), "subscription", subscription.getName()));
        } catch (UserNotFoundException |SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (ValidationInputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (DataAccessException | PersistenceException ex) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

    @DeleteMapping("{id}/subscriptions/{sub_id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") Long userId, @PathVariable("sub_id") Long subscriptionId) {
        try {
            userService.deleteSubscriptionById(userId, subscriptionId);
            return ResponseEntity.ok().body(Map.of("message", "the operation was completed successfully"));
        } catch (UserNotFoundException | SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (DataAccessException | PersistenceException ex) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }
    private static class SubscriptionRequestBody{
        @JsonProperty("name")
        private String name;

        private void validate() throws ValidationInputDataException{
            if (name == null || name.trim().isEmpty()) {
                throw new ValidationInputDataException("name", "name is null or empty");
            }
        }
    }

}

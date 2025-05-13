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

import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
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
    private static class UserRequestBody{
        @JsonProperty("username")
        private String username;

        private void validate() throws ValidationInputDataException{
            if (username == null || username.trim().isEmpty()) {
                throw new ValidationInputDataException("username", "username is null or empty");
            }
        }
        public String getUsername() {
            return username;
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
}

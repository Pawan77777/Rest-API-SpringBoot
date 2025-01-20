package com.springrest.springrest.controller;

import com.springrest.springrest.entities.User;
import com.springrest.springrest.exception.DuplicateUserException;
import com.springrest.springrest.exception.UserNotFoundException;
import com.springrest.springrest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class MyController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUserProfile() {
        List<User> users = this.userService.getUserProfile();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userId) {
        try {
            User user = this.userService.getUserProfileById(Long.parseLong(userId));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/users", consumes = "application/json")
    public ResponseEntity<User> addUserProfile(@RequestBody @Valid User user) {
        try {
            User createdUser = this.userService.addUserProfile(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DuplicateUserException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable long userId, @RequestBody @Valid User user) {
        try {
            user.setId(userId);
            User updatedUser = this.userService.updateUserProfile(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DuplicateUserException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<HttpStatus> deleteUserProfile(@PathVariable String userId) {
        try {
            this.userService.deleteUserProfile(Long.parseLong(userId));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
   
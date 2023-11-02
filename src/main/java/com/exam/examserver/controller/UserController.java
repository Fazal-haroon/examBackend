package com.exam.examserver.controller;

import com.exam.examserver.exception.UserFoundException;
import com.exam.examserver.model.UserRequestModel;
import com.exam.examserver.model.UserResponseModel;
import com.exam.examserver.entity.Role;
import com.exam.examserver.entity.User;
import com.exam.examserver.entity.UserRole;
import com.exam.examserver.repo.RoleRepository;
import com.exam.examserver.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/user/{username}")
    public UserResponseModel getUser(@PathVariable("username") String username) {
        return this.userServiceImpl.getUser(username);
    }

    //delete the user by id
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        this.userServiceImpl.deleteUser(userId);
    }

    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity<?> exceptionHandler(UserFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}

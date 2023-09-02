package com.exam.examserver.controller;

import com.exam.examserver.model.UserRequestModel;
import com.exam.examserver.model.UserResponseModel;
import com.exam.examserver.entity.Role;
import com.exam.examserver.entity.User;
import com.exam.examserver.entity.UserRole;
import com.exam.examserver.repo.RoleRepository;
import com.exam.examserver.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private RoleRepository roleRepository;

    //user creation
    @PostMapping("/user/")
    public User createUser(@RequestBody UserRequestModel user) throws Exception {
        Set<UserRole> roles = new HashSet<>();
        Role roleExist = roleRepository.findByRoleName("NORMAL");
        UserRole userRole = new UserRole();
        User user1 = userCreateMapping(user);
        if (roleExist == null) {
            Role role = new Role();
            role.setRoleId(0L);
            role.setRoleName("NORMAL");

            userRole.setUser(user1);
            userRole.setRole(role);
        } else {
            userRole.setUser(user1);
            userRole.setRole(roleExist);
        }
        roles.add(userRole);
        return this.userServiceImpl.createUser(user1, roles);
    }

    @GetMapping("/user/{username}")
    public UserResponseModel getUser(@PathVariable("username") String username) {
        return this.userServiceImpl.getUser(username);
    }

    //delete the user by id
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        this.userServiceImpl.deleteUser(userId);
    }

    private User userCreateMapping(UserRequestModel userRequestModel) {
        User user = new User();
        user.setUsername(userRequestModel.getUsername());
        user.setPassword(userRequestModel.getPassword());
        user.setFirstName(userRequestModel.getFirstName());
        user.setLastName(userRequestModel.getLastName());
        user.setEmail(userRequestModel.getEmail());
        user.setPhone(userRequestModel.getPhone());
        if (userRequestModel.getProfile() == null) {
            user.setProfile("default.png");
        } else {
            user.setProfile(userRequestModel.getProfile());
        }
        return user;
    }
}

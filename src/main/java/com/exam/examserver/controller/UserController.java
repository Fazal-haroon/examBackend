package com.exam.examserver.controller;

import com.exam.examserver.model.UserRequestModel;
import com.exam.examserver.model.UserResponseModel;
import com.exam.examserver.entity.Role;
import com.exam.examserver.entity.User;
import com.exam.examserver.entity.UserRole;
import com.exam.examserver.repo.RoleRepository;
import com.exam.examserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    //user creation
    @PostMapping("/")
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
        return this.userService.createUser(user1, roles);
    }

    @GetMapping("/{username}")
    public UserResponseModel getUser(@PathVariable("username") String username) {
        return this.userService.getUser(username);
    }

    //delete the user by id
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        this.userService.deleteUser(userId);
    }

    private User userCreateMapping(UserRequestModel userRequestModel){
        User user = new User();
        user.setUsername(userRequestModel.getUsername());
        user.setPassword(userRequestModel.getPassword());
        user.setFirstName(userRequestModel.getFirstName());
        user.setLastName(userRequestModel.getLastName());
        user.setEmail(userRequestModel.getEmail());
        user.setPhone(userRequestModel.getPhone());
        user.setProfile(userRequestModel.getProfile());
        return user;
    }
}

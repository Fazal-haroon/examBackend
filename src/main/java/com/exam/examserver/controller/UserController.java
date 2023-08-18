package com.exam.examserver.controller;

import com.exam.examserver.dto.UserRequestDto;
import com.exam.examserver.dto.UserResponseDto;
import com.exam.examserver.model.Role;
import com.exam.examserver.model.User;
import com.exam.examserver.model.UserRole;
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
    public User createUser(@RequestBody UserRequestDto user) throws Exception {
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
    public UserResponseDto getUser(@PathVariable("username") String username) {
        return this.userService.getUser(username);
    }

    private User userCreateMapping(UserRequestDto userRequestDto){
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhone(userRequestDto.getPhone());
        user.setProfile(userRequestDto.getProfile());
        return user;
    }
}

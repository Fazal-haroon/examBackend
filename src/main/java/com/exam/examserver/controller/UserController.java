package com.exam.examserver.controller;

import com.exam.examserver.model.Role;
import com.exam.examserver.model.User;
import com.exam.examserver.model.UserRole;
import com.exam.examserver.repo.RoleRepository;
import com.exam.examserver.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public User createUser(@RequestBody User user) throws Exception {

        Set<UserRole> roles = new HashSet<>();

        Role roleExist = roleRepository.findByRoleName("NORMAL");
        UserRole userRole = new UserRole();
        if (roleExist == null) {
            Role role = new Role();
            role.setRoleId(0L);
            role.setRoleName("NORMAL");

            userRole.setUser(user);
            userRole.setRole(role);
        } else {
            userRole.setUser(user);
            userRole.setRole(roleExist);
        }

        roles.add(userRole);

        return this.userService.createUser(user, roles);
    }
}

package com.exam.examserver.controller;

import com.exam.examserver.config.JwtUtil;
import com.exam.examserver.entity.*;
import com.exam.examserver.model.UserRequestModel;
import com.exam.examserver.repo.RoleRepository;
import com.exam.examserver.service.UserDetailsServiceImpl;
import com.exam.examserver.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@CrossOrigin
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private RoleRepository roleRepository;

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new Exception("User not Found.");
        }
        //authenticate
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtil.generateToken(userDetails);
//        return ResponseEntity.ok(new JwtResponse("Bearer " + token));
        return ResponseEntity.ok(new JwtResponse(token));

    }

    //user creation
    @PostMapping("/create")
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

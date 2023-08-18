package com.exam.examserver.service;

import com.exam.examserver.dto.UserResponseDto;
import com.exam.examserver.model.User;
import com.exam.examserver.model.UserRole;
import com.exam.examserver.repo.RoleRepository;
import com.exam.examserver.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //creating user
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
        User local = this.userRepository.findByUsername(user.getUsername());
        if (local != null) {
            System.out.println("User is already there !!");
            throw new Exception("User already present !!");
        } else {
            //create user
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.setId(0L);
            user.getUserRoles().addAll(userRoles);
            local = this.userRepository.save(user);
        }
        return local;
    }

    //getting user by username
    public UserResponseDto getUser(String username) {
        User byUsername = this.userRepository.findByUsername(username);
        UserResponseDto userMapping = getUserMapping(byUsername);
        return userMapping;
    }

    private UserResponseDto getUserMapping(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setPassword(user.getPassword());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setProfile(user.getProfile());
        userResponseDto.setEnabled(user.isEnabled());
        return userResponseDto;
    }


}

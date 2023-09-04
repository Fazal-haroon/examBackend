package com.exam.examserver.service;

import com.exam.examserver.entity.Authority;
import com.exam.examserver.model.UserResponseModel;
import com.exam.examserver.entity.User;
import com.exam.examserver.entity.UserRole;
import com.exam.examserver.repo.RoleRepository;
import com.exam.examserver.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl {

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
    public UserResponseModel getUser(String username) {
        User byUsername = this.userRepository.findByUsername(username);
        UserResponseModel userMapping = getUserMapping(byUsername);
        return userMapping;
    }

    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    private UserResponseModel getUserMapping(User user){
        UserResponseModel userResponseModel = new UserResponseModel();
        userResponseModel.setId(user.getId());
        userResponseModel.setUsername(user.getUsername());
        userResponseModel.setPassword(user.getPassword());
        userResponseModel.setFirstName(user.getFirstName());
        userResponseModel.setLastName(user.getLastName());
        userResponseModel.setEmail(user.getEmail());
        userResponseModel.setPhone(user.getPhone());
        userResponseModel.setProfile(user.getProfile());
        userResponseModel.setEnabled(user.isEnabled());
        userResponseModel.setAuthorities((Set<Authority>) user.getAuthorities());
        return userResponseModel;
    }


}

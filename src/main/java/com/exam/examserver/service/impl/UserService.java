package com.exam.examserver.service.impl;

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
}

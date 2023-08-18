package com.exam.examserver;

import com.exam.examserver.model.Role;
import com.exam.examserver.model.User;
import com.exam.examserver.model.UserRole;
import com.exam.examserver.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ExamserverApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ExamserverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("starting code");

		User user = new User();
		user.setFirstName("Fazal");
		user.setLastName("Haroon");
		user.setUsername("fazalharoon4");
		user.setPassword("abc");
		user.setEmail("abc@gmail.com");
		user.setProfile("default.png");

		Role roles1 = new Role();
		roles1.setRoleName("ADMIN");

		Set<UserRole> userRoleSet = new HashSet<>();

		UserRole userRole = new UserRole();
		userRole.setRole(roles1);
		userRole.setUser(user);

		userRoleSet.add(userRole);

		User user1 = this.userService.createUser(user, userRoleSet);
		System.out.println("user1 = " + user1.getUsername());

	}
}

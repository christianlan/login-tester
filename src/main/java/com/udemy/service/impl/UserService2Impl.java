package com.udemy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.udemy.entity.User;
import com.udemy.repository.UserRepository;
import com.udemy.service.UserService2;

@Service("userService2Impl")
public class UserService2Impl implements UserService2 {

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;

	@Override
	public User addUser(User user) { // For adding and editing purpose
		return userRepository.save(user);
	}

	@Override
	public List<User> listAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public void removeUser(String username) {
		userRepository.deleteById(username);
	}
	
}

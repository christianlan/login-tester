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
	public User addUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> listAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeUser(String username) {
		// TODO Auto-generated method stub
		
	}
	
}

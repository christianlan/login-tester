package com.udemy.service;

import java.util.List;

import com.udemy.entity.User;

public interface UserService2 {
	
	public abstract User addUser(User user);
	
	public abstract List<User> listAllUsers();
	
	public abstract User findUserByUsername(String username);
		
	public abstract void removeUser(String username);
}

package com.udemy.converter;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.udemy.entity.User;
import com.udemy.entity.UserRole;
import com.udemy.model.UserModel;

@Component("userConverter")
public class UserConverter {
	
//	@Autowired
//	@Qualifier("userRoleConverter")
//	private UserRoleConverter userRoleConverter;
//	
//	public User model2entity(UserModel userModel) {
//		User user = new User();
//		
//		user.setUsername(userModel.getUsername());
//		user.setPassword(userModel.getPassword());
//		user.setEnabled(userModel.isEnabled());
//		
//		Set<UserRole> userRoles = userRoleConverter.model2entity(userModel.getUserRolesModel());
//		
//		user.setUserRolesModel(userModel.getUserRolesModel());
//		user.setUse2fa(userModel.isUse2fa());
//		user.setSecret(userModel.getSecret());
//		
//		return user;
//	}
//	
//	public UserModel entity2model(User user) {
//		UserModel userModel = new UserModel();
//		
//		userModel.setUsername(user.getUsername());
//		userModel.setPassword(user.getPassword());
//		userModel.setEnabled(user.isEnabled());
//		userModel.setUserRolesModel(user.getUserRoles());
//		userModel.setUse2fa(user.isUse2fa());
//		userModel.setSecret(user.getSecret());
//		
//		return userModel;
//	}
}

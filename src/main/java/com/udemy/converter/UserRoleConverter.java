package com.udemy.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.udemy.entity.User;
import com.udemy.entity.UserRole;
import com.udemy.model.UserModel;
import com.udemy.model.UserRoleModel;

@Component("userRoleConverter")
public class UserRoleConverter {
	
//	@Autowired
//	@Qualifier("userConverter")
//	private UserConverter userConverter;
//	
//	public UserRole model2entity(UserRoleModel userRoleModel) {
//		UserRole userRole = new UserRole();
//		
//		userRole.setUserRoleId(userRoleModel.getUserRoleId());
//		
//		User user = userConverter.model2entity(userRoleModel.getUserModel());
//		userRole.setUser(user);
//		
//		userRole.setRole(userRoleModel.getRole());
//		
//		return userRole;
//	}
//	
//	public UserRoleModel entity2model(UserRole userRole) {
//		UserRoleModel userRoleModel = new UserRoleModel();
//		
//		userRoleModel.setUserRoleId(userRole.getUserRoleId());
//		
//		UserModel userModel = userConverter.entity2model(userRole.getUser());
//		userRoleModel.setUserModel(userModel);
//		
//		userRoleModel.setRole(userRole.getRole());
//		
//		return userRoleModel;
//	}
}

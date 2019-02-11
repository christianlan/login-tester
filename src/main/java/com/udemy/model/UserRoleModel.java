package com.udemy.model;

public class UserRoleModel {
	
	private Integer userRoleId;
	private UserModel userModel;
	private String role;
	
	public UserRoleModel(Integer userRoleId, UserModel userModel, String role) {
		super();
		this.userRoleId = userRoleId;
		this.userModel = userModel;
		this.role = role;
	}
	
	public UserRoleModel() {}
	
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public UserModel getUserModel() {
		return userModel;
	}
	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "UserRoleModel [userRoleId=" + userRoleId + ", userModel=" + userModel + ", role=" + role + "]";
	}
	
}

package com.udemy.model;

import java.util.Set;

import org.jboss.aerogear.security.otp.api.Base32;

public class UserModel {

	private String username;
	private String password;
	private boolean enabled;
	private Set<UserRoleModel> userRolesModel;
	private boolean use2fa;
	private String secret;
	
	public UserModel(String username, String password, boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.use2fa = false;
		this.secret = Base32.random();
	}
	
	public UserModel() {}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Set<UserRoleModel> getUserRolesModel() {
		return userRolesModel;
	}
	public void setUserRolesModel(Set<UserRoleModel> userRolesModel) {
		this.userRolesModel = userRolesModel;
	}
	public boolean isUse2fa() {
		return use2fa;
	}
	public void setUse2fa(boolean use2fa) {
		this.use2fa = use2fa;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}

package com.udemy.service;

import org.springframework.security.core.Authentication;

import com.udemy.entity.User;

public interface IAuthenticationFacade {
	Authentication getAuthentication();
	
	User getAuthenticatedUser();
}

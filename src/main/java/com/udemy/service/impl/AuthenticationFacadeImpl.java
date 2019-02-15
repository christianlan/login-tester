package com.udemy.service.impl;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.udemy.entity.User;
import com.udemy.service.IAuthenticationFacade;

@Component
public class AuthenticationFacadeImpl implements IAuthenticationFacade {

	@Override
	public Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) { // User is not authenticated yet
			authentication = null; 
		}
		
		return authentication;
	}

	@Override
	public User getAuthenticatedUser() {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) { // User has already been authenticated
			System.out.println("Retrieving the authenticated user...");
			user = (User) authentication.getPrincipal();
		}
		return user;
	}

}

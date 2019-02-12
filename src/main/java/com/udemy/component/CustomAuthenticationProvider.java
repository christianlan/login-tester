package com.udemy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.udemy.entity.User;
import com.udemy.repository.UserRepository;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
	
	@Autowired
	private UserRepository ur;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		
		User user = ur.findByUsername(auth.getName());		
		if (user == null) { // No debería ocurrir, ya que debe haberse hecho una comprobación previa para llegar aquí
			throw new BadCredentialsException("Invalid username or password xd");
		}
		
		Authentication result = super.authenticate(auth);
		return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

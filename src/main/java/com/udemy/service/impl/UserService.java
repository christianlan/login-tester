package com.udemy.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.udemy.constant.ViewConstant;
import com.udemy.entity.UserRole;
import com.udemy.repository.UserRepository;

@Service("userService")
public class UserService implements UserDetailsService{

	@Autowired
	@Qualifier("userRepository")
	private UserRepository ur;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		com.udemy.entity.User user2 = new com.udemy.entity.User();
//		user2.setUsername("user");
//		user2.setPassword(new BCryptPasswordEncoder().encode("pass"));
//		user2.setEnabled(true);
//		
//		Set<UserRole> roles = new HashSet<UserRole>();
//		roles.add(new UserRole(1, user2, "role_user"));
//		user2.setUserRole(roles);
		// ---------------------------------------------
		// com.udemy.entity.User user3 = ViewConstant.user;
		
		com.udemy.entity.User user = ur.findByUsername(username);
		List<GrantedAuthority> authorities = buildAuthorities(user.getUserRoles());
		return buildUser(user, authorities);
	}

	private User buildUser(com.udemy.entity.User user, List<GrantedAuthority> authorities) {
		return new User(user.getUsername(), user.getPassword(), user.isEnabled(), 
				true, true, true, authorities);
	}
	
	private List<GrantedAuthority> buildAuthorities(Set<UserRole> userRoles) {
		Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
		
		for (UserRole userRole: userRoles) {
			auths.add(new SimpleGrantedAuthority(userRole.getRole()));
		}
		return new ArrayList<GrantedAuthority>(auths);
	}
	
	public String generateQRUrl(com.udemy.entity.User user) throws UnsupportedEncodingException{
		String qr_prefix = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
		String app_name = "springRegistration";
		
	    return qr_prefix + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", app_name, user.getUsername(), user.getSecret(), app_name), "UTF-8");
	}
}

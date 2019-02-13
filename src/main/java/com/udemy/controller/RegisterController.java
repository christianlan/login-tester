package com.udemy.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.udemy.constant.ViewConstant;
import com.udemy.entity.User;
import com.udemy.entity.UserRole;
import com.udemy.repository.UserRepository;
import com.udemy.service.UserService2;
import com.udemy.service.impl.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("userService2Impl")
	private UserService2 userService2;
	
	@Autowired
	private UserRepository ur;
	
	private static final Log LOG = LogFactory.getLog(RegisterController.class);
	
	@GetMapping("/register")
	public String showRegisterForm(@RequestParam(name="message", required=false) String message, Model model) {
		LOG.info("METHOD: showRegisterForm");
		
		model.addAttribute("message", message);

		model.addAttribute("user", new User());
		return ViewConstant.REGISTER;
	}
	
	@PostMapping("/registerConfirmation")
	public String ConfirmRegister(@ModelAttribute("user") User user, HttpServletRequest request, Model model) {
		// The model attribute User contains username, password and use2fa
		if (checkUsername(user.getUsername())) { // If the username doesn't exist
			if (user.getPassword().equals(request.getParameter("password2"))) {
				LOG.info("Register new user: " + user.toString());
				
				user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
				user.setEnabled(true);
				
				if (user.isUse2fa()) {
					user.setSecret(Base32.random()); //Generate and set the secret token
					
					try {
						String qrurl = userService.generateQRUrl(user);
						model.addAttribute("qr", qrurl);
						model.addAttribute("user", user);
												
						return ViewConstant.QR_CODE;
						
					} catch (UnsupportedEncodingException e) {
//						user.setUse2fa(false); // Forzar que no se use el 2fa				
						return "redirect:/login?message=Encoding not supported, registration canceled automatically";
					}
				}
				else {
					setRolesForUser(user);
					userService2.addUser(user); // Register the user to DB
					return "redirect:/login?new_user=" + user.getUsername();
				}
			} else {
				return "redirect:/register/register?message=The repeated password is different from the password";
			}
		} else {
			return "redirect:/register/register?message=The username " + user.getUsername() + " already exists";
		}
	}
	
	private boolean checkUsername(String username) {
		User user = ur.findByUsername(username);
		return user==null;
	}
	
	@PostMapping("/validatecode")
	public String validateCode(HttpServletRequest request, Model model) {
		String verificationCode = request.getParameter("code");
		String secret = request.getParameter("secret");
		
		User user = new User();
		user.setUsername(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		user.setEnabled(new Boolean(request.getParameter("enabled")));
		user.setUse2fa(new Boolean(request.getParameter("use2fa")));
		user.setSecret(request.getParameter("secret"));
		
		Totp totp = new Totp(secret);
		if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) { // When the code is incorrect, go to failure url
			model.addAttribute("error", true);
			
			String qrurl = request.getParameter("qr");
			model.addAttribute("qr", qrurl);
			
			model.addAttribute("user", user);
			
			return ViewConstant.QR_CODE;
		} else { 
			setRolesForUser(user);
			userService2.addUser(user); // Register the user to DB
			return "redirect:/login?new_user=" + user.getUsername();
		}
	}
	
	@GetMapping("/cancel")
	public String cancelRegistration(HttpServletRequest request) {
//		String username = request.getParameter("username");
//		LOG.info("Cancelling the registration for the user: " + username);
		return "redirect:/login?message=You cancelled the registration";
	}
	
	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private void setRolesForUser(User user) {
		Set<UserRole> roles = new HashSet<UserRole>();
		roles.add(new UserRole(user, "role_user"));
		user.setUserRoles(roles);
	}
}

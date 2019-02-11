package com.udemy.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.udemy.constant.ViewConstant;
import com.udemy.entity.User;
import com.udemy.repository.UserRepository;

@Controller
public class LoginController {

	private static final Log LOG = LogFactory.getLog(LoginController.class);
	
	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;
	
	@GetMapping("/login")
	public String showLoginForm(Model model,
			@RequestParam(name="error", required=false) String error,
			@RequestParam(name="logout", required=false) String logout,
			@RequestParam(name="new_user", required=false) String new_user,
			@RequestParam(name="message", required=false) String message) {
		
		LOG.info("----------METHOD: showLoginForm");
		System.out.println("PARAMS: error=" + error + ", logout=" + logout + ", new_user=" + new_user + ", message=" + message);
		
		model.addAttribute("error", error);
		model.addAttribute("logout", logout);
		model.addAttribute("new_user", new_user);
		model.addAttribute("message", message);
		
		model.addAttribute("usercheck_flag", true);
		model.addAttribute("authUser", this.getUser());
		
		LOG.info("---------------GO TO LOGIN PAGE");
		return ViewConstant.LOGIN;
	}
	
	@PostMapping("/login/usercheck")
	public String userCheck(HttpServletRequest request, Model model) {
		String given_username = request.getParameter("username");
		LOG.info("Checking the introduced user: " + given_username);
		
		User user = userRepository.findByUsername(given_username);
		if (user == null) {
			return "redirect:/login?message=" + given_username + " doesn't exist";
		} else {
			model.addAttribute("given_username", given_username);
			model.addAttribute("code_flag", user.isUse2fa());
			model.addAttribute("usercheck_flag", false);
			return ViewConstant.LOGIN;
		}
	}
	
	@GetMapping("/loginsuccess")
	public String loginSuccess() {
		LOG.info("---------------GO TO CONTACTS PAGE");
		return "redirect:/contacts/showcontacts";
	}
	
	private User getUser() {
		User user = null;
		
		Object o = SecurityContextHolder.getContext().getAuthentication();
		boolean auth = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
		boolean anonymous =  (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken); 
		if (o != null && auth && !anonymous) {
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		return user;
	}
}

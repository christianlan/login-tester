package com.udemy.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.udemy.constant.ViewConstant;
import com.udemy.entity.User;
import com.udemy.service.UserService2;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private static final Log LOG = LogFactory.getLog(UserController.class);
	
	@Autowired
	@Qualifier("userService2Impl")
	private UserService2 userService2;
	
	@GetMapping("/showusers")
	public String showUsers(Model model) {
		model.addAttribute("users", userService2.listAllUsers());
		
		LOG.info("show the user list");
		return ViewConstant.USERS;
	}
	
	@GetMapping("/userform")
	public String showUserForm(@RequestParam(name="id", required=true) String username, Model model) {
		User user = userService2.findUserByUsername(username);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("password", user.getPassword());
		model.addAttribute("enabled", user.isEnabled());
		return ViewConstant.USER_FORM;
	}
	
	@PostMapping("/edituser")
	public String editUser(HttpServletRequest request, Model model) {
		// Retrieve the user with his id (username)
		String username = request.getParameter("username");
		User user = userService2.findUserByUsername(username);
		
		LOG.info("user: " + user.toString());
		
		// System.out.println(request.getParameter("enabled"));
		// Modify those attributes that are desired to be edited
		boolean enabled = request.getParameter("enabled").equalsIgnoreCase("on");
		user.setEnabled(enabled);
		
		// Save the user to DB
		userService2.addUser(user);
		
		return "redirect:/users/showusers";
	}
	
	@GetMapping("/removeuser")
	public String removeUser(@RequestParam(name="id", required=true) String username, Model model) {
		userService2.removeUser(username);
		return "redirect:/users/showusers";
	}
}

package com.udemy.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
//	@Autowired
//	private ICaptchaService captchaService;
	
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

		if (checkUsername(user.getUsername())) { // If the username doesn't exist
			if (user.getPassword().equals(request.getParameter("password2"))) {
				LOG.info("Register new user: " + user.toString());
				
				user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
				user.setEnabled(true);
				
				Set<UserRole> roles = new HashSet<UserRole>();
				roles.add(new UserRole(user, "role_user"));
				user.setUserRoles(roles);
				
				userService2.addUser(user);
				
				if (user.isUse2fa()) {
					try {
						String qrurl = userService.generateQRUrl(user);
						model.addAttribute("qr", qrurl);
						model.addAttribute("username", user.getUsername());
						
						// return "redirect:/qrcode.html?lang=" + request.getLocale().getLanguage();
						
						return ViewConstant.QR_CODE;
						
					} catch (UnsupportedEncodingException e) {
						// model.addAttribute("message", "Encoding not supported, 2fa disabled to the user");
						// model.addAttribute("new_user", user.getUsername());
						user.setUse2fa(false); // Forzar que no se use el 2fa				
						return "redirect:/login?new_user=" + user.getUsername() + "?message=Encoding not supported, 2fa disabled to the user";
					}
					//return "redirect:/register/qrcode?username=" + user.getUsername();
				} else {
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
	
	
//	@GetMapping("/qrcode")
//	public String showQRcode(@RequestParam("username") String username, Model model) {
//		User user = userService2.findUserByUsername(username);
//		try {
//			String qrurl = userService.generateQRUrl(user);
//			model.addAttribute("qr", qrurl);
//			model.addAttribute("new_user", user.getUsername());
//			
//			// LOG.info("QR url: " + qrurl);
//			// return "redirect:/qrcode.html?lang=" + request.getLocale().getLanguage();
//			
//			return "qrcode";
//			
//		} catch (UnsupportedEncodingException e) {
//			// model.addAttribute("message", "Encoding not supported, 2fa disabled to the user");
//			// model.addAttribute("new_user", user.getUsername());
//			user.setUse2fa(false); // Forzar que no se use el 2fa
//			
//			LOG.info("Error encoding in showQRcode method");
//			return "redirect:/login?new_user=" + username + "?message=Encoding not supported, 2fa disabled to the user";
//		}
//	}
}

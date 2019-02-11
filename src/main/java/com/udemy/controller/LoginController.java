package com.udemy.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@PostMapping("/login/credentialscheck")
	public String userCheck(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		String given_username = request.getParameter("username");
		LOG.info("Checking the introduced user: " + given_username);
		
		User user = userRepository.findByUsername(given_username);
		if (user == null) {
			return "redirect:/login?message=" + given_username + " doesn't exist"; // Debería cambiar el text al "Invalid username or password por la seguridad"
		} else {
			String given_password = request.getParameter("password");
			boolean password_match = new BCryptPasswordEncoder().matches(given_password, user.getPassword());
			
			if (password_match) {
//				if (user.isUse2fa()) {
					model.addAttribute("given_username", given_username);
					model.addAttribute("given_password", given_password);
					model.addAttribute("usercheck_flag", false);
					model.addAttribute("code_flag", false);
					model.addAttribute("message", "The credentials are verified, now you can click the button below to login");
					return ViewConstant.LOGIN;
//				} else {
//					redirectAttributes.addAttribute("request", request);
//					redirectAttributes.addAttribute("response", response);
//					redirectAttributes.addAttribute("username", given_username);
//					redirectAttributes.addAttribute("password", given_password);
//					
//					return "redirect:/login/without2fa";
//				}
			} else {
				return "redirect:/login?message=The password is incorrect"; // Debería cambiar el text al "Invalid username or password por la seguridad"
			}
		}
	}
	
//	@PostMapping("/login/without2fa")
//	private void loginWithout2fa(RedirectAttributes redirectAttributes) throws ServletException, IOException {
//		Map<String, Object> map = redirectAttributes.asMap();
//		
//		HttpServletRequest request = (HttpServletRequest) map.get("request");
//		HttpServletResponse response = (HttpServletResponse) map.get("response");
//		String username = (String) map.get("username");
//		String password = (String) map.get("password");
//		
//		request.setAttribute("username", username);
//		request.setAttribute("password", password);
//		
//		request.getRequestDispatcher("/logincheck").forward(request, response);
//	}
	
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

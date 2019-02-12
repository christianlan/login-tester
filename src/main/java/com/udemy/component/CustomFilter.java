package com.udemy.component;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.core.context.SecurityContextHolder;

import com.udemy.entity.User;

public class CustomFilter implements Filter {

	private static final Log LOG = LogFactory.getLog(CustomFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
        LOG.info("Starting a transaction for req : {}" + req.getRequestURI());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (req.getRequestURI().equals("/loginsuccess")) { // loginsuccess (después de autenticarse con username y password)
	        if (user.isUse2fa()) {
		        req.getRequestDispatcher("/login/verificationcode").forward(req, res);
	        }
		    else { // Directly go to success url
		    	req.getRequestDispatcher("/login/check2succ").forward(req, res);
			} 
        } 
        else if (req.getRequestURI().equals("/logincheck2")) { // logincheck_phase2: comprobar el código de seguridad 
        	LOG.info("checking the 2fa for the user: " + user.getUsername());

			String verificationCode = req.getParameter("code");

			Totp totp = new Totp(user.getSecret());
			if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) { // When the code is incorrect, go to failure url
				LOG.info("Invalid verification code");
				req.getRequestDispatcher("/login/check2fail").forward(req, res);
			} else { // Go to success url
				req.getRequestDispatcher("/login/check2succ").forward(req, res);
			}
        } 
        else { // It's not possible to reach here, because there is not another url configured but the above two.
        	LOG.info("This is an error url: " + req.getRequestURI());
        	req.getRequestDispatcher("/login/check2fail").forward(req, res);
        }
        
        // chain.doFilter(request, response);
		// LOG.info("Committing a transaction for req : {}" + req.getRequestURI());
	}
	
	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}

package com.udemy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.udemy.component.CustomAuthenticationProvider;
import com.udemy.component.CustomFilter;
import com.udemy.component.CustomWebAuthenticationDetailsSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	@Qualifier("userService")
	private UserDetailsService userService;
	
	@Autowired
	private CustomWebAuthenticationDetailsSource cwADS;
	
	public SecurityConfiguration() {
		super();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/css/**", "imgs/**", "/register/**", "/login*", "/login/**").permitAll()
			.anyRequest().authenticated()
		.and()
			.formLogin() // Declares UsernamePasswordAuthenticationFilter and DefaultLoginPageGeneratingFilter
			.loginPage("/login") // Explicitly declares the login page url, if not, the default login page will be used
			.loginProcessingUrl("/logincheck")
			.authenticationDetailsSource(cwADS)
			.failureUrl("/login?error=true")
			.defaultSuccessUrl("/loginsuccess").permitAll()
		.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/login?logout").permitAll();	
	}
	
    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }
	
	@Bean
	public FilterRegistrationBean<CustomFilter> registerCustomFilter() {
		FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<>();
		
		registrationBean.setFilter(new CustomFilter());
		registrationBean.addUrlPatterns("/loginsuccess");
		registrationBean.addUrlPatterns("/logincheck2");
		
		return registrationBean;
	}
}

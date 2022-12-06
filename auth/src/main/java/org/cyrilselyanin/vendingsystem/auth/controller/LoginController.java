package org.cyrilselyanin.vendingsystem.auth.controller;


import org.cyrilselyanin.vendingsystem.auth.config.AuthProperties;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	private final AuthProperties authProperties;

	public LoginController(AuthProperties authProperties) {
		this.authProperties = authProperties;
	}

	@GetMapping("/login")
	public String showLoginPage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:" + authProperties.getClientUserDefaultUrl();
		}
		return "login";
	}

}

package org.cyrilselyanin.vendingsystem.auth.controller;

import org.cyrilselyanin.vendingsystem.auth.config.AuthProperties;
import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.domain.VendingSystemUser;
import org.cyrilselyanin.vendingsystem.auth.service.ProfileService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {

	private final String PASSWORDS_NOT_EQUAL_MESSAGE = "Пароли должны совпадать.";
	private final String USER_ALREADY_EXISTS_MESSAGE = "Данный пользователь уже существует.";

	private final JdbcUserDetailsManager jdbcUserDetailsManager;
	private final PasswordEncoder passwordEncoder;
	private final ProfileService profileService;
	private final AuthProperties authProperties;

	public RegisterController(
			JdbcUserDetailsManager jdbcUserDetailsManager,
			PasswordEncoder passwordEncoder,
			ProfileService profileService,
			AuthProperties authProperties
	) {
		this.jdbcUserDetailsManager = jdbcUserDetailsManager;
		this.passwordEncoder = passwordEncoder;
		this.profileService = profileService;
		this.authProperties = authProperties;
	}

	@GetMapping("/register")
	public String showRegisterPage(VendingSystemUser vendingSystemUser) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:" + authProperties.getClientUserDefaultUrl();
		}
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(
			@Valid VendingSystemUser vendingSystemUser,
			BindingResult result,
			Model model
	) {
		if (!vendingSystemUser.getPassword().equals(
				vendingSystemUser.getPassword2()
		)) {
			ObjectError error = new ObjectError(
					"vendingSystemUser",
					PASSWORDS_NOT_EQUAL_MESSAGE
			);
			result.addError(error);
		}

		if (jdbcUserDetailsManager.userExists(vendingSystemUser.getUsername())) {
			ObjectError error = new ObjectError(
					"vendingSystemUser",
					USER_ALREADY_EXISTS_MESSAGE
			);
			result.addError(error);
		}

		if (result.hasErrors()) {
			return "register";
		}
		// https://www.baeldung.com/spring-thymeleaf-error-messages

		String username = vendingSystemUser.getUsername();
		String password = passwordEncoder.encode(vendingSystemUser.getPassword());

		UserDetails userDetails = User
			.withUsername(username)
			.password(password)
			.roles("USER")
			.build();
		jdbcUserDetailsManager.createUser(userDetails);

		Profile profile = new Profile();
		profile.setUsername(username);
		profileService.createOne(profile);

		return "redirect:/login";
	}

}

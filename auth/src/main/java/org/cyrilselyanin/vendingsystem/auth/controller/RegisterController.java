package org.cyrilselyanin.vendingsystem.auth.controller;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.auth.config.AuthProperties;
import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.dto.RegisterUserDto;
import org.cyrilselyanin.vendingsystem.auth.helper.UserDetailsFactory;
import org.cyrilselyanin.vendingsystem.auth.service.ProfileService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@RequiredArgsConstructor
@Controller
public class RegisterController {

	private static final String PASSWORDS_NOT_EQUAL_MESSAGE = "Пароли должны совпадать.";
	private static final String USER_ALREADY_EXISTS_MESSAGE = "Данный пользователь уже существует.";

	private final JdbcUserDetailsManager jdbcUserDetailsManager;
	private final PasswordEncoder passwordEncoder;
	private final ProfileService profileService;
	private final AuthProperties authProperties;
	private final UserDetailsFactory userDetailsFactory;

	@GetMapping("/register")
	public String showRegisterPage(RegisterUserDto user) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:" + authProperties.getClientUserDefaultUrl();
		}
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(
			@Valid RegisterUserDto user,
			BindingResult result,
			Model model
	) {
		if (!user.getPassword().equals(
				user.getPassword2()
		)) {
			ObjectError error = new ObjectError(
					"user",
					PASSWORDS_NOT_EQUAL_MESSAGE
			);
			result.addError(error);
		}

		if (jdbcUserDetailsManager.userExists(user.getUsername())) {
			ObjectError error = new ObjectError(
					"user",
					USER_ALREADY_EXISTS_MESSAGE
			);
			result.addError(error);
		}

		if (result.hasErrors()) {
			return "register";
		}

		String username = user.getUsername();
		String password = passwordEncoder.encode(user.getPassword());

		UserDetails userDetails = userDetailsFactory.createUserDetailsForUser(
				username,
				password
		);
		jdbcUserDetailsManager.createUser(userDetails);

		Profile profile = new Profile();
		profile.setUsername(username);
		profileService.createOne(profile);

		return "redirect:/login";
	}

}

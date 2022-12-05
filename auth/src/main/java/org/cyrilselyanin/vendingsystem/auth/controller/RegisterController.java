package org.cyrilselyanin.vendingsystem.auth.controller;

import org.cyrilselyanin.vendingsystem.auth.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {

	private final String PASSWORDS_NOT_EQUAL_STRING = "Пароли должны совпадать";

	@GetMapping("/register")
	public String showRegisterPage(User user) {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid User user, BindingResult result, Model model) {
		if (!user.getPassword().equals(user.getPassword2())) {
			ObjectError error = new ObjectError("user", PASSWORDS_NOT_EQUAL_STRING);
			result.addError(error);
		}

		if (result.hasErrors()) {
			return "register";
		}
		// https://www.baeldung.com/spring-thymeleaf-error-messages

		return "login";
	}

}

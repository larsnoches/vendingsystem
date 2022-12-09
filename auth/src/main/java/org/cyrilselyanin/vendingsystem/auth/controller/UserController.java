package org.cyrilselyanin.vendingsystem.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final JdbcUserDetailsManager jdbcUserDetailsManager;

	public void createUser() {
		//
	}

	public void getUser() {
		//
	}

	public void getAllUsers() {
		//
	}

	public void updateUser() {
		//
	}

	public void removeUser() {
		//
	}

	public void getAuthority() {
//		jdbcUserDetailsManager.
	}

}

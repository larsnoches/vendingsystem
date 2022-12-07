package org.cyrilselyanin.vendingsystem.auth.helper;

import org.cyrilselyanin.vendingsystem.auth.dto.CreateUserDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsBuilder {

	private UserDetailsBuilder() {
		//
	}

	public static UserDetails createUserDetailsFromDto(CreateUserDto dto) {
		UserDetails userDetails;
		if (dto.getIsManager()) {
			userDetails = UserDetailsBuilder.createUserDetailsForManager(
					dto.getUsername(),
					dto.getPassword()
			);
		} else {
			userDetails = UserDetailsBuilder.createUserDetailsForUser(
					dto.getUsername(),
					dto.getPassword()
			);
		}
		return userDetails;
	}

	public static UserDetails createUserDetailsForUser(
			String username,
			String password
	) {
		return User
				.withUsername(username)
				.password(password)
				.roles("USER")
				.build();
	}

	public static UserDetails createUserDetailsForManager(
			String username,
			String password
	) {
		return User
				.withUsername(username)
				.password(password)
				.roles("USER", "MANAGER")
				.build();
	}

}

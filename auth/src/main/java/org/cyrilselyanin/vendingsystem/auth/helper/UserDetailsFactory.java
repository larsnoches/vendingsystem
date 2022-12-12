package org.cyrilselyanin.vendingsystem.auth.helper;

import lombok.NoArgsConstructor;
import org.cyrilselyanin.vendingsystem.auth.dto.CreateUserDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserDetailsFactory {

	public UserDetails createUserDetailsFromDto(CreateUserDto dto) {
		UserDetails userDetails;
		if (dto.getIsManager()) {
			userDetails = createUserDetailsForManager(
					dto.getUsername(),
					dto.getPassword(),
					dto.getIsEnabled()
			);
		} else {
			userDetails = createUserDetailsForUser(
					dto.getUsername(),
					dto.getPassword(),
					dto.getIsEnabled()
			);
		}
		return userDetails;
	}

	public UserDetails createUserDetails(
			String username,
			String password,
			Boolean isEnabled,
			Boolean isManager
	) {
		UserDetails userDetails;
		if (Boolean.TRUE.equals(isManager)) {
			userDetails = createUserDetailsForManager(
					username,
					password,
					isEnabled
			);
		} else {
			userDetails = createUserDetailsForUser(
					username,
					password,
					isEnabled
			);
		}
		return userDetails;
	}

	public UserDetails createUserDetailsForUser(
			String username,
			String password
	) {
		return createUserDetailsForUser(username, password, true);
	}

	public UserDetails createUserDetailsForUser(
			String username,
			String password,
			Boolean isEnabled
	) {
		return User
				.withUsername(username)
				.password(password)
				.roles("USER")
				.disabled(!isEnabled)
				.build();
	}

	public UserDetails createUserDetailsForManager(
			String username,
			String password
	) {
		return createUserDetailsForManager(username, password, true);
	}

	public UserDetails createUserDetailsForManager(
			String username,
			String password,
			Boolean isEnabled
	) {
		return User
				.withUsername(username)
				.password(password)
				.roles("USER", "MANAGER")
				.disabled(!isEnabled)
				.build();
	}

}

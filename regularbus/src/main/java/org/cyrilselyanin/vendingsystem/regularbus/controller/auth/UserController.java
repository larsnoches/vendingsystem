package org.cyrilselyanin.vendingsystem.regularbus.controller.auth;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.User;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	@GetMapping("/what")
	public String what() {
		final Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();

		StringBuilder sb = new StringBuilder();
		authentication.getAuthorities().forEach(s -> sb.append(s.getAuthority()));
		return sb.toString();
	}

	@GetMapping("/users")
	public Page<GetUserResponseDto> getUsers(Pageable pageable) {
		return userService.getUsers(pageable);
	}

	@PostMapping("/users/save")
	public ResponseEntity<User> saveUser(
			@RequestBody @Valid User user
	) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/api/v1/users/save")
						.toUriString()
		);
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}

	@PostMapping("/users/register")
	public ResponseEntity<RegistrationResponseDto> registerUser(
			@RequestBody @Valid RegistrationRequestDto requestDto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/users/register")
							.toUriString()
			);
			return ResponseEntity.created(uri).body(
					userService.registerUser(requestDto)
			);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

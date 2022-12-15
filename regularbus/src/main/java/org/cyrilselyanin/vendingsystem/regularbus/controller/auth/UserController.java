package org.cyrilselyanin.vendingsystem.regularbus.controller.auth;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.User;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

//	@GetMapping("/what")
//	public String what() {
//		final Authentication authentication = SecurityContextHolder
//				.getContext()
//				.getAuthentication();
//
//		StringBuilder sb = new StringBuilder();
//		authentication.getAuthorities().forEach(s -> sb.append(s.getAuthority()));
//		return sb.toString();
//	}

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

}

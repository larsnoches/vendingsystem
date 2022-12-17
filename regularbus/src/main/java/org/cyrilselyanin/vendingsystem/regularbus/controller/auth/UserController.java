package org.cyrilselyanin.vendingsystem.regularbus.controller.auth;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.ChangePasswordRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.CreateUserRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.UpdateUserRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class UserController {

	private static final String WRONG_USER_ID_ERR_MESSAGE = "Недопустимый id пользователя.";
	private static final String WRONG_EMAIL_ERR_MESSAGE = "Такой адрес электронной почты недопустим.";
	private static final String BLANK_EMAIL_ERR_MESSAGE = "Адрес электронной почты не может быть пустым.";
	private static final String LENGTH_EMAIL_ERR_MESSAGE = "Адрес электронной почты должен быть от 6 до 255 символов.";

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

	@GetMapping("/users/{id}")
	public GetUserResponseDto getUserById(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		return userService.getUser(id);
	}

	@GetMapping("/users/get/{email}")
	public GetUserResponseDto getUserByEmail(
			@NotBlank(message = BLANK_EMAIL_ERR_MESSAGE)
			@Size(
					min = 6,
					max = 255,
					message = LENGTH_EMAIL_ERR_MESSAGE)
			@Email(message = WRONG_EMAIL_ERR_MESSAGE)
			@PathVariable String email
	) {
		return userService.getUser(email);
	}

	@PostMapping("/users/create")
	public ResponseEntity<GetUserResponseDto> createUser(
			@RequestBody @Valid CreateUserRequestDto createUserRequestDto
	) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/api/v1/users/create")
						.toUriString()
		);
		return ResponseEntity
				.created(uri)
				.body(userService.createUser(createUserRequestDto));
	}

	@PutMapping("/users/{id}/update")
	public GetUserResponseDto updateUserById(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid UpdateUserRequestDto updateUserRequestDto
	) {
		return userService.updateUserById(id, updateUserRequestDto);
	}

	@PutMapping("/users/{id}/changepassword")
	public void changeUserPassword(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto
	) {
		userService.changePassword(id, changePasswordRequestDto);
	}

	@PostMapping("/users/{id}/remove")
	public void removeUser(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		userService.removeUser(id);
	}

}

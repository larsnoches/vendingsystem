package org.cyrilselyanin.vendingsystem.regularbus.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.ChangePasswordRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.CreateUserRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.UpdateUserRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
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
		try {
			return userService.getUsers(pageable);
		} catch (RuntimeException ex) {
			log.error("There is a get users error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/users/{id}")
	public GetUserResponseDto getUserById(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			return userService.getUser(id);
		} catch (RuntimeException ex) {
			log.error("There is a get user by id error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
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
		try {
			return userService.getUser(email);
		} catch (RuntimeException ex) {
			log.error("There is a get user by email error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PostMapping("/users/create")
	public ResponseEntity<GetUserResponseDto> createUser(
			@RequestBody @Valid CreateUserRequestDto createUserRequestDto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/users/create")
							.toUriString()
			);
			return ResponseEntity
					.created(uri)
					.body(userService.createUser(createUserRequestDto));

		} catch (RuntimeException ex) {
			log.error("There is a create user error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/users/{id}/update")
	public GetUserResponseDto updateUserById(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid UpdateUserRequestDto updateUserRequestDto
	) {
		try {
			return userService.updateUserById(id, updateUserRequestDto);
		} catch (RuntimeException ex) {
			log.error("There is an update user by id error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/users/{id}/changepassword")
	public void changeUserPassword(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto
	) {
		try {
			userService.changePassword(id, changePasswordRequestDto);
		} catch (RuntimeException ex) {
			log.error("There is a change user password error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/users/{id}/remove")
	public void removeUser(
			@NotNull
			@Min(value = 0L, message = WRONG_USER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			userService.removeUser(id);
		} catch (RuntimeException ex) {
			log.error("There is a remove user error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

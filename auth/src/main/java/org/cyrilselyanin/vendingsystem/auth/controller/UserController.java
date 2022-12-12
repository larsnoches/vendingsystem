package org.cyrilselyanin.vendingsystem.auth.controller;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.auth.dto.CreateUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.GetOrUpdateProfileDto;
import org.cyrilselyanin.vendingsystem.auth.dto.GetUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.UpdateUserDto;
import org.cyrilselyanin.vendingsystem.auth.helper.NotFoundException;
import org.cyrilselyanin.vendingsystem.auth.helper.UserShared;
import org.cyrilselyanin.vendingsystem.auth.service.ProfileService;
import org.cyrilselyanin.vendingsystem.auth.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api")
@Validated
@CrossOrigin("http://127.0.0.1:4200")
public class UserController {

	private static final String USERNAME_SHOULD_NOTBE_EMPTY_MESSAGE = "Имя пользователя не может быть пустым.";
	private static final String USERNAME_MAXSIZE_MESSAGE = "Имя пользователя должно быть до 50 символов.";

	private final UserService userService;
	private final ProfileService profileService;

	@GetMapping("/what")
	public String getAllUsers() {
		final Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();
		return authentication.getName();
	}

	@PostMapping("/users")
	public GetUserDto createUser(
			@Valid
			@RequestBody CreateUserDto dto
	) {
		return userService.createOne(dto);
	}

//	@PreAuthorize("hasRole('manager')")
//	@PreAuthorize("hasAnyRole('USER', 'MANAGER')")
	@GetMapping("/users")
	public Page<GetUserDto> getAllUsers(Pageable pageable) {
		return userService.getAllUsers(pageable);
	}

	@GetMapping("/users/{username}")
	public UpdateUserDto getUser(
			@NotBlank(message = USERNAME_SHOULD_NOTBE_EMPTY_MESSAGE)
			@Size(max = 50, message = USERNAME_MAXSIZE_MESSAGE)
			@Pattern(regexp = UserShared.USERNAME_REGEXP_PATTERN)
			@PathVariable String username
	) {
		return userService.getOne(username);
	}

	@PutMapping("/users/{username}")
	public void updateUser(
			@NotBlank(message = USERNAME_SHOULD_NOTBE_EMPTY_MESSAGE)
			@Size(max = 50, message = USERNAME_MAXSIZE_MESSAGE)
			@Pattern(regexp = UserShared.USERNAME_REGEXP_PATTERN)
			@PathVariable String username,
			@Valid
			@RequestBody UpdateUserDto dto
	) {
		userService.updateOne(username, dto);
	}

	@PutMapping("/users/{username}/changepassword")
	public void changePassword(
			@NotBlank(message = USERNAME_SHOULD_NOTBE_EMPTY_MESSAGE)
			@Size(max = 50, message = USERNAME_MAXSIZE_MESSAGE)
			@Pattern(regexp = UserShared.USERNAME_REGEXP_PATTERN)
			@PathVariable String username,
			@Valid
			@RequestBody UpdateUserDto dto
	) {
		userService.changePassword(username, dto);
	}

	@GetMapping("/users/{username}/profile")
	public GetOrUpdateProfileDto getProfile(
			@NotBlank(message = USERNAME_SHOULD_NOTBE_EMPTY_MESSAGE)
			@Size(max = 50, message = USERNAME_MAXSIZE_MESSAGE)
			@Pattern(regexp = UserShared.USERNAME_REGEXP_PATTERN)
			@PathVariable String username
	) {
		if (!userService.userExists(username)) {
			throw new NotFoundException(UserShared.NO_SUCH_USER_MESSAGE);
		}
		return profileService.getOne(username);
	}

	@PutMapping("/users/{username}/profile")
	public void updateProfile(
			@NotBlank(message = USERNAME_SHOULD_NOTBE_EMPTY_MESSAGE)
			@Size(max = 50, message = USERNAME_MAXSIZE_MESSAGE)
			@Pattern(regexp = UserShared.USERNAME_REGEXP_PATTERN)
			@PathVariable String username,
			@Valid
			@RequestBody GetOrUpdateProfileDto dto
	) {
		if (!userService.userExists(username)) {
			throw new NotFoundException(UserShared.NO_SUCH_USER_MESSAGE);
		}
		profileService.updateOne(username, dto);
	}

	@DeleteMapping("/users/{username}")
	public void removeUser(
			@NotBlank(message = USERNAME_SHOULD_NOTBE_EMPTY_MESSAGE)
			@Size(max = 50, message = USERNAME_MAXSIZE_MESSAGE)
			@Pattern(regexp = UserShared.USERNAME_REGEXP_PATTERN)
			@PathVariable String username
	) {
		userService.deleteOne(username);
	}

}

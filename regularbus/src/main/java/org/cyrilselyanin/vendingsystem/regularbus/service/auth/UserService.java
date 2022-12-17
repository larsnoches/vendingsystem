package org.cyrilselyanin.vendingsystem.regularbus.service.auth;

import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

	@PreAuthorize("hasRole('MANAGER')")
	GetUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

	@PreAuthorize("hasRole('MANAGER') or (hasRole('USER') and principal.username == updateUserRequestDto.email)")
	GetUserResponseDto updateUserById(Long id, UpdateUserRequestDto updateUserRequestDto);

	@PreAuthorize("hasRole('MANAGER') or (hasRole('USER') and principal.username == changePasswordRequestDto.email)")
	void changePassword(Long id, ChangePasswordRequestDto changePasswordRequestDto);

	@PreAuthorize("hasRole('MANAGER') or (hasRole('USER') and principal.username == email)")
	GetUserResponseDto getUser(String email);

	@PreAuthorize("hasRole('MANAGER')")
	GetUserResponseDto getUser(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetUserResponseDto> getUsers(Pageable pageable);

	RegistrationResponseDto registerUser(RegistrationRequestDto requestDto);

	@PreAuthorize("hasRole('MANAGER')")
	void removeUser(Long id);

}

package org.cyrilselyanin.vendingsystem.regularbus.service.auth;

import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.CuUserRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

	GetUserResponseDto createUser(CuUserRequestDto cuUserRequestDto);
	GetUserResponseDto updateUser(Long id, CuUserRequestDto cuUserRequestDto);
	GetUserResponseDto getUser(String email);
	GetUserResponseDto getUser(Long id);
	@PreAuthorize("hasRole('MANAGER')")
	Page<GetUserResponseDto> getUsers(Pageable pageable);
	RegistrationResponseDto registerUser(RegistrationRequestDto requestDto);

}

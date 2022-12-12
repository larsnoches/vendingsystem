package org.cyrilselyanin.vendingsystem.regularbus.service.auth;

import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.User;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

	User saveUser(User user);
	User getUser(String email);
	@PreAuthorize("hasRole('MANAGER')")
	Page<GetUserResponseDto> getUsers(Pageable pageable);
	RegistrationResponseDto registerUser(RegistrationRequestDto requestDto);

}

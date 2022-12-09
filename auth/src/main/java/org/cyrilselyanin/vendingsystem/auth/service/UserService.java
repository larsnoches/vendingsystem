package org.cyrilselyanin.vendingsystem.auth.service;

import org.cyrilselyanin.vendingsystem.auth.dto.ChangePasswordDto;
import org.cyrilselyanin.vendingsystem.auth.dto.CreateOrUpdateUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.GetUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User service
 */

public interface UserService {

	GetUserDto createOne(CreateOrUpdateUserDto dto);

	/**
	 * Get user info for edit user form
	 * @param username User's name
	 * @return Dto for form
	 */
	CreateOrUpdateUserDto getOne(String username);
	Page<GetUserDto> getAllUsers(Pageable pageable);
	void updateOne(CreateOrUpdateUserDto dto);
	void changePassword(ChangePasswordDto dto);
	void deleteOne(String username);

}

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

	/**
	 * Create user by manager
	 * @param dto Dto
	 * @return Dto for response with user info
	 */
	GetUserDto createOne(CreateOrUpdateUserDto dto);

	/**
	 * Get user info for edit user form
	 * @param username User's name
	 * @return Dto for form
	 */
	CreateOrUpdateUserDto getOne(String username);

	/**
	 * List of users info
	 * @param pageable Pageable object
	 * @return Page with users info
	 */
	Page<GetUserDto> getAllUsers(Pageable pageable);

	/**
	 * Update user by manager
	 * @param dto Dto
	 */
	void updateOne(CreateOrUpdateUserDto dto);

	/**
	 * Change password by self user
	 * @param dto Dto
	 */
	void changePassword(ChangePasswordDto dto);

	/**
	 * Delete user by manager
	 * @param username Username
	 */
	void deleteOne(String username);

}

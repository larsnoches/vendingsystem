package org.cyrilselyanin.vendingsystem.auth.service;

import org.cyrilselyanin.vendingsystem.auth.dto.CreateUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.GetUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.UpdateUserDto;
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
	GetUserDto createOne(CreateUserDto dto);

	/**
	 * Get user info for edit user form
	 * @param username User's name
	 * @return Dto for form
	 */
	UpdateUserDto getOne(String username);

	/**
	 * List of users info by manager
	 * @param pageable Pageable object
	 * @return Page with users info
	 */
	Page<GetUserDto> getAllUsers(Pageable pageable);

	/**
	 * Update user by manager
	 * @param dto Dto
	 */
	void updateOne(String username, UpdateUserDto dto);

	/**
	 * Change password by self user
	 * @param dto Dto
	 */
	void changePassword(String username, UpdateUserDto dto);

	/**
	 * Delete user by manager
	 * @param username Username
	 */
	void deleteOne(String username);

	/**
	 * Check if user exists
	 * @param username Username
	 * @return result
	 */
	Boolean userExists(String username);

}

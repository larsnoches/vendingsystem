package org.cyrilselyanin.vendingsystem.auth.service;

import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.dto.GetOrUpdateProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Vending system user profile service
 */
public interface ProfileService {

	/**
	 * Create profile related with user creation
	 * @param profile Profile entity
	 * @return Profile entity
	 */
	Profile createOne(Profile profile);

	/**
	 * Get profile info by self user
	 * @param username Username
	 * @return Dto for profile
	 */
	GetOrUpdateProfileDto getOne(String username);

	/**
	 * Get profile entities related with getting user list
	 * @param pageable Pageable
	 * @return Profile entities
	 */
	Page<Profile> getAll(Pageable pageable);

	/**
	 * Update profile by self user
	 * @param dto Dto
	 */
	void updateOne(String username, GetOrUpdateProfileDto dto);

	/**
	 * Delete profile by manager
	 * @param username
	 */
	void deleteOne(String username);
}

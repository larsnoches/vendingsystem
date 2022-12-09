package org.cyrilselyanin.vendingsystem.auth.service;

import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Vending system user profile service
 */
public interface ProfileService {
	Profile createOne(Profile profile);

	Optional<Profile> getOne(String username);
	Page<Profile> getAll(Pageable pageable);

	void updateOne(Profile profile);

	void deleteOne(String username);
}

package org.cyrilselyanin.vendingsystem.auth.service;

import org.cyrilselyanin.vendingsystem.auth.domain.Profile;

import java.util.List;
import java.util.Optional;

/**
 * User profile service
 */
public interface ProfileService {
	Profile createOne(Profile profile);

	Optional<Profile> getOne(String username);
	List<Profile> getAll();

	Profile updateOne(Profile profile);

	void deleteOne(Profile profile);
}

package org.cyrilselyanin.vendingsystem.auth.service;

import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User profile service implementation
 */
@Service
public class ProfileServiceImpl implements ProfileService{

	private final ProfileRepository profileRepository;

	public ProfileServiceImpl(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@Override
	public Profile createOne(Profile profile) {
		return profileRepository.save(profile);
	}

	@Override
	public Optional<Profile> getOne(String username) {
		return Optional.empty();
	}

	@Override
	public List<Profile> getAll() {
		return null;
	}

	@Override
	public Profile updateOne(Profile profile) {
		return null;
	}

	@Override
	public void deleteOne(Profile profile) {

	}
}

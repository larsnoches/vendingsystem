package org.cyrilselyanin.vendingsystem.auth.service;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Vending system user profile service implementation
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;

	@Override
	public Profile createOne(Profile profile) {
		return profileRepository.save(profile);
	}

	@Override
	public Optional<Profile> getOne(String username) {
		return profileRepository.findByUsername(username);
	}

	@Override
	public List<Profile> getAll() {
		return null;
	}

	@Override
	public void updateOne(Profile profile) {
		profileRepository.save(profile);
	}

	@Override
	public void deleteOne(String username) {
		profileRepository.deleteByUsername(username);
	}
}

package org.cyrilselyanin.vendingsystem.auth.service;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.dto.GetOrUpdateProfileDto;
import org.cyrilselyanin.vendingsystem.auth.helper.*;
import org.cyrilselyanin.vendingsystem.auth.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Vending system user profile service implementation
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

	private final String NO_SUCH_PROFILE_MESSAGE = "Данный профиль не существует.";

	private final ProfileRepository profileRepository;
	private final UserDataMapper userDataMapper;
	private final AuthorityMatcher authorityMatcher;

	@Override
	public Profile createOne(Profile profile) {
		return profileRepository.save(profile);
	}

	@Override
	public GetOrUpdateProfileDto getOne(String username) {
		if (!authorityMatcher.isManager() && !authorityMatcher.isThatUser(username)) {
			throw new AuthException(UserShared.NO_USER_ACCESS_MESSAGE);
		}
		Profile profile = profileRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException(NO_SUCH_PROFILE_MESSAGE));
		return userDataMapper.toGetOrUpdateProfileDto(profile);
	}

	@Override
	public Page<Profile> getAll(Pageable pageable) {
		return profileRepository.findAll(pageable);
	}

	@Override
	public void updateOne(String username, GetOrUpdateProfileDto dto) {
		if (!authorityMatcher.isManager() && !authorityMatcher.isThatUser(username)) {
			throw new AuthException(UserShared.NO_USER_ACCESS_MESSAGE);
		}
		if (authorityMatcher.isThatUser(username) && !dto.getUsername().equals(username)) {
			throw new AuthException(UserShared.WRONG_USER_MESSAGE);
		}
		Profile profile = userDataMapper.fromGetOrUpdateProfileDto(dto);
		profileRepository.save(profile);
	}

	@Override
	public void deleteOne(String username) {
		profileRepository.deleteByUsername(username);
	}

}

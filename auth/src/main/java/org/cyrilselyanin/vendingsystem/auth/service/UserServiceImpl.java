package org.cyrilselyanin.vendingsystem.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.dto.CreateUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.GetUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.UpdateUserDto;
import org.cyrilselyanin.vendingsystem.auth.helper.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final JdbcUserDetailsManager jdbcUserDetailsManager;
	private final ProfileService profileService;
	private final UserDataMapper userDataMapper;
	private final UserDetailsFactory userDetailsFactory;
	private final AuthorityMatcher authorityMatcher;

	@Override
	public GetUserDto createOne(CreateUserDto dto) {
		if (jdbcUserDetailsManager.userExists(dto.getUsername())) {
			throw new AuthException(UserShared.USER_ALREADY_EXISTS_MESSAGE);
		}
		if (StringUtils.isEmpty(dto.getPassword())) {
			throw new AuthException(UserShared.NO_EMPTY_PASSWORD_MESSAGE);
		}

		UserDetails userDetails = userDetailsFactory.createUserDetailsFromDto(dto);
		jdbcUserDetailsManager.createUser(userDetails);

		Profile profile = Profile.builder()
				.username(dto.getUsername())
				.build();
		profileService.createOne(profile);

		return userDataMapper.toGetUserDto(profile);
	}

	@Override
	public UpdateUserDto getOne(String username) {
		if (!authorityMatcher.isManager() && !authorityMatcher.isThatUser(username)) {
			throw new AuthException(UserShared.NO_USER_ACCESS_MESSAGE);
		}
		if (!jdbcUserDetailsManager.userExists(username)) {
			throw new NotFoundException(UserShared.NO_SUCH_USER_MESSAGE);
		}
		UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(username);
		UpdateUserDto userDto = new UpdateUserDto();
		userDto.setIsEnabled(userDetails.isEnabled());

		Boolean isManager = authorityMatcher.isManager(userDetails.getAuthorities());
		userDto.setIsManager(isManager);
		return userDto;
	}

	@Override
	public Page<GetUserDto> getAllUsers(Pageable pageable) {
		List<GetUserDto> list = profileService.getAll(pageable).stream()
				.map(userDataMapper::toGetUserDto)
				.collect(Collectors.toList());
		return new PageImpl<>(list);
	}

	@Override
	public void updateOne(String username, UpdateUserDto dto) {
		if (!jdbcUserDetailsManager.userExists(username)) {
			throw new NotFoundException(UserShared.NO_SUCH_USER_MESSAGE);
		}

		UserDetails oldUserDetails = jdbcUserDetailsManager.loadUserByUsername(username);
		String password = oldUserDetails.getPassword();
		if (StringUtils.isNotEmpty(dto.getPassword())) {
			password = dto.getPassword();
		}

		UserDetails userDetails = userDetailsFactory.createUserDetails(
				username, password, dto.getIsEnabled(), dto.getIsManager()
		);

		jdbcUserDetailsManager.updateUser(userDetails);
	}

	@Override
	public void changePassword(String username, UpdateUserDto dto) {
		if (!authorityMatcher.isManager() && !authorityMatcher.isThatUser(username)) {
			throw new AuthException(UserShared.NO_USER_ACCESS_MESSAGE);
		}
		if (!jdbcUserDetailsManager.userExists(username)) {
			throw new NotFoundException(UserShared.NO_SUCH_USER_MESSAGE);
		}

		UserDetails oldUserDetails = jdbcUserDetailsManager.loadUserByUsername(username);
		String oldPassword = oldUserDetails.getPassword();
		String password = dto.getPassword();

		jdbcUserDetailsManager.changePassword(oldPassword, password);
	}

	@Override
	public void deleteOne(String username) {
		if (!jdbcUserDetailsManager.userExists(username)) {
			throw new NotFoundException(UserShared.NO_SUCH_USER_MESSAGE);
		}
		jdbcUserDetailsManager.deleteUser(username);
	}

	@Override
	public Boolean userExists(String username) {
		return jdbcUserDetailsManager.userExists(username);
	}

}

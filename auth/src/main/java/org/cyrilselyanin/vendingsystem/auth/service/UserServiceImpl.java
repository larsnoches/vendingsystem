package org.cyrilselyanin.vendingsystem.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.dto.ChangePasswordDto;
import org.cyrilselyanin.vendingsystem.auth.dto.CreateOrUpdateUserDto;
import org.cyrilselyanin.vendingsystem.auth.dto.GetUserDto;
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

	private final String USER_ALREADY_EXISTS_MESSAGE = "Данный пользователь уже существует.";
	private final String NO_SUCH_USER_MESSAGE = "Данный пользователь не существует.";
	private final String NO_EMPTY_PASSWORD_MESSAGE = "Пароль не может быть пустым.";

	private final JdbcUserDetailsManager jdbcUserDetailsManager;
	private final ProfileService profileService;
	private final UserDataMapper userDataMapper;
	private final UserDetailsFactory userDetailsFactory;
	private final AuthorityMatcher authorityMatcher;

	@Override
	public GetUserDto createOne(CreateOrUpdateUserDto dto) {
		if (jdbcUserDetailsManager.userExists(dto.getUsername())) {
			throw new AuthException(USER_ALREADY_EXISTS_MESSAGE);
		}
		if (StringUtils.isEmpty(dto.getPassword())) {
			throw new AuthException(NO_EMPTY_PASSWORD_MESSAGE);
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
	public CreateOrUpdateUserDto getOne(String username) {
		UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(username);
		CreateOrUpdateUserDto userDto = new CreateOrUpdateUserDto();
		userDto.setUsername(userDetails.getUsername());
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
	public void updateOne(CreateOrUpdateUserDto dto) {
		if (!jdbcUserDetailsManager.userExists(dto.getUsername())) {
			throw new NotFoundException(NO_SUCH_USER_MESSAGE);
		}

		UserDetails oldUserDetails = jdbcUserDetailsManager.loadUserByUsername(dto.getUsername());
		String password = oldUserDetails.getPassword();
		if (StringUtils.isNotEmpty(dto.getPassword())) {
			password = dto.getPassword();
		}

		UserDetails userDetails = userDetailsFactory.createUserDetails(
				dto.getUsername(), password, dto.getIsEnabled(), dto.getIsManager()
		);

		jdbcUserDetailsManager.updateUser(userDetails);
	}

	@Override
	public void changePassword(ChangePasswordDto dto) {
		if (!jdbcUserDetailsManager.userExists(dto.getUsername())) {
			throw new NotFoundException(NO_SUCH_USER_MESSAGE);
		}

		UserDetails oldUserDetails = jdbcUserDetailsManager.loadUserByUsername(dto.getUsername());
		String oldPassword = oldUserDetails.getPassword();
		String password = dto.getPassword();

		jdbcUserDetailsManager.changePassword(oldPassword, password);
	}

	@Override
	public void deleteOne(String username) {
		if (!jdbcUserDetailsManager.userExists(username)) {
			throw new NotFoundException(NO_SUCH_USER_MESSAGE);
		}
		jdbcUserDetailsManager.deleteUser(username);
	}

}

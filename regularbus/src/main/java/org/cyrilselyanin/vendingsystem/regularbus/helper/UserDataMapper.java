package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.User;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.UserRole;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

	private final ModelMapper modelMapper;
	private final Converter<UserRole, String> userRoleConverter = r -> r
			.getSource()
			.name();
	private final Converter<UserRole, Boolean> isManagerUserRoleConverter = r -> r
			.getSource()
			.name()
			.equals(UserRole.ROLE_MANAGER.name());

	public UserDataMapper() {
		modelMapper = new ModelMapper();

		modelMapper.createTypeMap(User.class, RegistrationResponseDto.class)
				.addMappings(mapper -> mapper.using(userRoleConverter).map(
						User::getUserRole, RegistrationResponseDto::setUserRole
				));

		modelMapper.createTypeMap(User.class, GetUserResponseDto.class)
				.addMappings(mapper -> mapper.using(isManagerUserRoleConverter).map(
						User::getUserRole, GetUserResponseDto::setIsManager
				));

		TypeMap<UpdateUserRequestDto, User> typeMap = modelMapper.createTypeMap(
				UpdateUserRequestDto.class, User.class
		);
		typeMap.include(CreateUserRequestDto.class, User.class);
		modelMapper.typeMap(CreateUserRequestDto.class, User.class)
				.addMapping(CreateUserRequestDto::getPassword, User::setPassword);
	}

	public RegistrationResponseDto toRegistrationResponseDto(User user) {
		return modelMapper.map(user, RegistrationResponseDto.class);
	}

	public GetUserResponseDto toGetUserResponseDto(User user) {
		return modelMapper.map(user, GetUserResponseDto.class);
	}

	public User fromRegistrationRequestDto(RegistrationRequestDto dto) {
		return modelMapper.map(dto, User.class);
	}

	public User fromCreateUserRequestDto(CreateUserRequestDto dto) {
		return modelMapper.map(dto, User.class);
	}

	public User fromUpdateUserRequestDto(UpdateUserRequestDto dto) {
		return modelMapper.map(dto, User.class);
	}

}

package org.cyrilselyanin.vendingsystem.regularbus.helper;

import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.User;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.UserRole;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.CuUserRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.RegistrationResponseDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

	private final ModelMapper modelMapper;
	private Converter<UserRole, Boolean> userRoleConverter = r -> r
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
				.addMappings(mapper -> mapper.using(userRoleConverter).map(
						User::getUserRole, GetUserResponseDto::setIsManager
				));
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

	public User fromCuUserRequestDto(CuUserRequestDto dto) {
		return modelMapper.map(dto, User.class);
	}

}

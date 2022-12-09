package org.cyrilselyanin.vendingsystem.auth.helper;

import org.cyrilselyanin.vendingsystem.auth.domain.Profile;
import org.cyrilselyanin.vendingsystem.auth.dto.GetOrUpdateProfileDto;
import org.cyrilselyanin.vendingsystem.auth.dto.GetUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

	private final ModelMapper modelMapper;

	public UserDataMapper() {
		modelMapper = new ModelMapper();
	}

	public GetUserDto toGetUserDto(Profile profile) {
		return modelMapper.map(profile, GetUserDto.class);
	}

	public GetOrUpdateProfileDto toGetOrUpdateProfileDto(Profile profile) {
		return modelMapper.map(profile, GetOrUpdateProfileDto.class);
	}

	public Profile fromGetUserDto(GetUserDto dto) {
		return modelMapper.map(dto, Profile.class);
	}

	public Profile fromGetOrUpdateProfileDto(GetOrUpdateProfileDto dto) {
		return modelMapper.map(dto, Profile.class);
	}

}

package org.cyrilselyanin.vendingsystem.regularbus.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponseDto {

	private String accessToken;
	private String refreshToken;

}

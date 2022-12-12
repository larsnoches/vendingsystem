package org.cyrilselyanin.vendingsystem.regularbus.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationResponseDto {

	private Long id;
	private String email;
	private String userRole;

}

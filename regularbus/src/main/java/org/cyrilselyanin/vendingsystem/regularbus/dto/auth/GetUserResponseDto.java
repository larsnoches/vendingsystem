package org.cyrilselyanin.vendingsystem.regularbus.dto.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
		"id", "email", "lastname", "firstname", "middlename", "isEnabled", "isManager"
})
public class GetUserResponseDto extends UpdateUserRequestDto {

	private Long id;

}

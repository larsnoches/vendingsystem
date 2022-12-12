package org.cyrilselyanin.vendingsystem.auth.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Create or update user request DTO
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDto implements Serializable {

	@Size(
			min = 4,
			max = 68,
			message = "Пароль должен быть от 4 до 68 символов.")
	private String password;

	private Boolean isManager;

	private Boolean isEnabled;

}

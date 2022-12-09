package org.cyrilselyanin.vendingsystem.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
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
public class CreateOrUpdateUserDto implements Serializable {

	@NotBlank(message = "Имя пользователя не может быть пустым.")
	@Size(
			min = 2,
			max = 50,
			message = "Имя пользователя должно быть от 2 до 50 символов.")
	private String username;

	@Size(
			min = 4,
			max = 68,
			message = "Пароль должен быть от 4 до 68 символов.")
	private String password;

	private Boolean isManager;

	private Boolean isEnabled;

}

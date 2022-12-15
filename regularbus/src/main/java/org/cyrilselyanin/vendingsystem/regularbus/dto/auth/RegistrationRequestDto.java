package org.cyrilselyanin.vendingsystem.regularbus.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequestDto {

	@NotBlank(message = "Адрес электронной почты не может быть пустым.")
	@Size(
			min = 6,
			max = 255,
			message = "Адрес электронной почты должен быть от 6 до 255 символов.")
	@Email(message = "Такой адрес электронной почты недопустим.")
	private String email;

	@NotBlank(message = "Пароль не может быть пустым.")
	@Size(
			min = 4,
			max = 68,
			message = "Пароль должен быть от 4 до 68 символов.")
	private String password;

}

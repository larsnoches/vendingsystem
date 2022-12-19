package org.cyrilselyanin.vendingsystem.regularbus.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class UpdateUserRequestDto {

	@NotBlank(message = "Адрес электронной почты не может быть пустым.")
	@Size(
			min = 6,
			max = 255,
			message = "Адрес электронной почты должен быть от 6 до 255 символов.")
	@Email(message = "Такой адрес электронной почты недопустим.")
	private String email;

	@Size(
			max = 255,
			message = "Фамилия должна быть не более 255 символов")
	private String lastname;

	@Size(
			max = 255,
			message = "Имя должно быть не более 255 символов")
	private String firstname;

	@Size(
			max = 255,
			message = "Отчество должно быть не более 255 символов")
	private String middlename;

	private Boolean isEnabled = true;
	private Boolean isManager = false;

}

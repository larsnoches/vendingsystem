package org.cyrilselyanin.vendingsystem.regularbus.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class CuUserRequestDto {

	@NotBlank(message = "Адрес электронной почты не может быть пустым.")
	@Size(
			min = 6,
			max = 255,
			message = "Адрес электронной почты должен быть от 6 до 255 символов.")
	@Email(message = "Такой адрес электронной почты недопустим.")
	@Column(nullable = false)
	private String email;

	@NotBlank(message = "Пароль не может быть пустым.")
	@Size(
			min = 4,
			max = 68,
			message = "Пароль должен быть от 4 до 68 символов.")
	@Column(nullable = false)
	private String password;

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

	private Boolean enabled = true;
	private Boolean isManager = false;

}

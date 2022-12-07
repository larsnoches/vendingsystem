package org.cyrilselyanin.vendingsystem.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Basic user DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class BasicUserDto implements Serializable {

	@NotBlank(message = "Имя пользователя не может быть пустым.")
	@Size(
			min = 2,
			max = 50,
			message = "Имя пользователя должно быть от 2 до 50 символов.")
	private String username;

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

	@Size(
			max = 255,
			message = "Email должен быть не более 255 символов")
	private String email;

	@Size(
			max = 20,
			message = "Номер телефона должен быть не более 20 символов")
	private String phone;

	private Boolean isManager;

}

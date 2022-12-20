package org.cyrilselyanin.vendingsystem.regularbus.dto.carrier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class BasicCarrierRequestDto {

	@NotBlank(message = "Имя не может быть пустым")
	@Size(
			min = 2,
			max = 255,
			message = "Имя должно быть от 2 до 255 символов")
	private String name;

	@NotBlank(message = "ИНН не указан")
	@Size(
			min = 6,
			max = 16,
			message = "Некорректный ИНН"
	)
	@Pattern(regexp = "^[0-9]{1,16}$") // digits in a string
	private String inn;

	@NotBlank(message = "Адрес не может быть пустым")
	@Size(
			min = 7,
			max = 255,
			message = "Адрес должен быть от 7 до 255 символов")
	private String address;

}

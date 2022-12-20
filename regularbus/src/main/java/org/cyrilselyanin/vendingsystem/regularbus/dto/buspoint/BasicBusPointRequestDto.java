package org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class BasicBusPointRequestDto {

	@NotBlank(message = "Имя не может быть пустым")
	@Size(
			min = 2,
			max = 255,
			message = "Имя должно быть от 2 до 255 символов")
	private String name;

	@NotBlank(message = "Адрес не может быть пустым")
	@Size(
			min = 2,
			max = 255,
			message = "Адрес должен быть от 2 до 255 символов")
	private String address;

	@NotBlank(message = "Тип остановочного пункта не может быть пустым")
	private String busPointType;

}

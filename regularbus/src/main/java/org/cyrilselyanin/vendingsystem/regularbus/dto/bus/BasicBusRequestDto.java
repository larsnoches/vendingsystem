package org.cyrilselyanin.vendingsystem.regularbus.dto.bus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class BasicBusRequestDto {

	@NotBlank(message = "Марка и модель не могу быть пустыми")
	@Size(
			min = 2,
			max = 255,
			message = "Марка и модель должны быть от 2 до 255 символов")
	private String makeModel;

	@NotBlank(message = "Страна-изготовитель не может быть пустой")
	@Size(
			min = 2,
			max = 255,
			message = "Страна-изготовитель должна быть от 2 до 255 символов")
	private String manufacturerCountry;

	@NotNull(message = "Год выпуска не указан")
	@Min(value = 1920, message = "Год выпуска должен быть больше")
	@Digits(
			integer = 4,
			fraction = 0,
			message = "Год выпуска должен быть длиной в 4 цифры"
	)
	private Integer yearOfManufacture;

	@NotNull(message = "Количество мест не указано")
	@Min(value = 2, message = "Количество мест должно быть больше 1")
	@Max(value = 200, message = "Количество мест должно быть меньше 200")
	private Integer seatCount;

	@NotBlank(message = "Государственный номер не указан")
	@Size(
			min = 4,
			max = 16,
			message = "Государственный номер должен быть от 4 до 16 символов")
	private String regNumber;

	@NotNull(message = "Перевозчик не указан")
	private Long carrier;

}

package org.cyrilselyanin.vendingsystem.regularbus.dto.fare;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class BasicFareRequestDto {

	@NotBlank(message = "Название тарифа не может быть пустым")
	@Size(
			min = 2,
			max = 255,
			message = "Название тарифа должно быть от 2 до 255 символов")
	private String name;

	@NotNull(message = "Цена тарифа не указана")
	@DecimalMin(
			value = "0.0",
			inclusive = false,
			message = "Цена тарифа должна быть больше")
	private BigDecimal price;

	// carrier id only
	@NotNull(message = "Перевозчик не указан")
	private Long carrier;

}

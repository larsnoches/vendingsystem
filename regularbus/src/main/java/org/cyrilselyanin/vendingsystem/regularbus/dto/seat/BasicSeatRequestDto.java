package org.cyrilselyanin.vendingsystem.regularbus.dto.seat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class BasicSeatRequestDto {

	@NotBlank(message = "Название места не заполнено")
	@Size(
			min = 1,
			max = 10,
			message = "Название места должно быть от 1 до 10 символов")
	private String name;

	@NotNull(message = "Состояние места не заполнено")
	private Boolean seatIsOccupied;

	@NotNull(message = "Маршрут не указан")
	private Long busTrip;

}

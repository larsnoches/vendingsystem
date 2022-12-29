package org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class BasicBusTripRequestDto {

	// departureBusPoint id only
	@NotNull(message = "Пункт отправления не указан")
	private Long departureBusPoint;

	// arrivalBusPoint id only
	@NotNull(message = "Пункт прибытия не указан")
	private Long arrivalBusPoint;

	@NotNull(message = "Расстояние не указано")
	@DecimalMin(
			value = "0.1",
			inclusive = true,
			message = "Расстояние должно быть больше")
	private BigDecimal distance;

	@NotBlank(message = "Номер маршрута не указан")
	@Size(
			min = 2,
			max = 15,
			message = "Номер маршрута должен быть от 2 до 15 символов")
	private String busRouteNumber;

	@NotBlank(message = "Дата отправления не указана")
	private String departureDate;

	@NotBlank(message = "Время отправления не указано")
	private String departureTime;

	@NotBlank(message = "Дата прибытия не указана")
	private String arrivalDate;

	@NotBlank(message = "Время прибытия не указано")
	private String arrivalTime;

//	@NotNull(message = "Средняя скорость на маршруте не указана")
//	@Min(value = 5, message = "Средняя скорость на рейсе должна быть более 5")
//	@Max(value = 130, message = "Средняя скорость на рейсе должна быть меньше 130")
//	private Integer averageBusSpeed;

	// bus id only
	@NotNull(message = "Автобус не указан")
	private Long bus;

	// fare id only
	@NotNull(message = "Тариф не указан")
	private Long fare;

	// carrier id only
	@NotNull(message = "Перевозчик не указан")
	private Long carrier;

	@JsonIgnore
	public String getDepartureDatetime() {
		return String.format("%s %s", departureDate, departureTime);
	}

	@JsonIgnore
	public String getArrivalDatetime() {
		return String.format("%s %s", arrivalDate, arrivalTime);
	}
}

package org.cyrilselyanin.vendingsystem.regularbus.dto.ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class BasicTicketRequestDto {

//	@NotNull(message = "Дата и время оформления не заполнены")
//	@FutureOrPresent
//	private Timestamp issueDateTime;

	@NotBlank(message = "Фамилия пассажира не заполнена")
	@Size(
			min = 2,
			max = 255,
			message = "Фамилия пассажира должна быть от 2 до 255 символов")
	private String passengerLastname;

	@NotBlank(message = "Имя пассажира не заполнено")
	@Size(
			min = 2,
			max = 255,
			message = "Имя пассажира должно быть от 2 до 255 символов")
	private String passengerFirstname;

	@Size(
			min = 2,
			max = 255,
			message = "Отчество пассажира должно быть от 2 до 255 символов")
	private String passengerMiddlename;

//	@NotBlank(message = "Номер маршрута автобуса не заполнен")
//	@Size(
//			min = 2,
//			max = 15,
//			message = "омер маршрута автобуса должен быть от 2 до 15 символов")
//	private String busRouteNumber;
//
//	@NotBlank(message = "QR-код не заполнен")
//	@Size(
//			min = 2,
//			max = 255,
//			message = "QR-код должен быть от 2 до 255 символов")
//	private String qrCode;

	@NotBlank(message = "Место не заполнено")
	@Size(
			min = 1,
			max = 10,
			message = "Место должно быть от 1 до 10 символов")
	private String seatName;

//	@NotBlank(message = "Имя перевозчика не заполнено")
//	@Size(
//			min = 2,
//			max = 255,
//			message = "Имя перевозчика должно быть от 2 до 255 символов")
//	private String carrierName;

//	@NotBlank(message = "Пункт отправления не указан")
//	@Size(
//			min = 2,
//			max = 255,
//			message = "Пункт отправления должен быть от 2 до 255 символов")
//	private String departureBuspointName;
//
//	@NotBlank(message = "Пункт прибытия не указан")
//	@Size(
//			min = 2,
//			max = 255,
//			message = "Пункт прибытия должен быть от 2 до 255 символов")
//	private String arrivalBuspointName;
//
//	@NotNull(message = "Дата и время отправления не указаны")
//	@Future
//	private Timestamp departureDateTime;
//
//	@NotNull(message = "Дата и время прибытия не указаны")
//	@Future
//	private Timestamp arrivalDateTime;
//
//	@NotNull(message = "Цена не указана")
//	@DecimalMin(
//			value = "0.0",
//			inclusive = false,
//			message = "Значение цены должно быть больше 0")
//	private BigDecimal price;

	@NotNull(message = "Маршрут не указан")
	private Long busTrip;

	private String email;

}

package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.BasicSeatRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.seat.GetSeatResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.SeatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class SeatController {

	private static final String WRONG_BUSTRIP_ID_ERR_MESSAGE = "Недопустимый id маршрута.";
	private static final String WRONG_BUS_ID_ERR_MESSAGE = "Недопустимый id автобуса.";
	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";
	private static final String WRONG_SEAT_ID_ERR_MESSAGE = "Недопустимый id места.";

	private final SeatService seatService;

	@PostMapping("/seats/create")
	public ResponseEntity<?> createSeat(
			@RequestBody @Valid BasicSeatRequestDto dto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/seats/create")
							.toUriString()
			);
			seatService.createSeat(dto);
			return ResponseEntity
					.created(uri)
					.build();
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/seats/busTrip/{busTripId}/list")
	public List<GetSeatResponseDto> getSeats(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable
			Long busTripId,
			@NotNull
			@Min(value = 0L, message = WRONG_BUS_ID_ERR_MESSAGE)
			@RequestParam("busId")
			Long busId,
			@NotBlank(message = "Дата отправления не указана")
			@RequestParam("departureDate")
			String departureDateString,
			@NotBlank(message = "Время отправления не указано")
			@RequestParam("departureTime")
			String departureTimeString
	) {
		try {
			String departureDateTime = String.format("%s %s", departureDateString, departureTimeString);
			return seatService.getSeats(busTripId, busId, departureDateTime);
		} catch (RuntimeException ex) {
			log.error("There is a get bustrips by id, departure date and time error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/seats/busTrip/{busTripId}")
	public Page<GetSeatResponseDto> getSeatsByBusTripId(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable
			Long busTripId,
			Pageable pageable
	) {
		try {
			return seatService.getSeatsByBusTripId(busTripId, pageable);
		} catch (RuntimeException ex) {
			log.error("There is a get bustrips by id, departure date and time error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/seats/{id}")
	public GetSeatResponseDto getSeat(
			@NotNull
			@Min(value = 0L, message = WRONG_SEAT_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			return seatService.getSeat(id);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/seats/{id}")
	public void updateSeat(
			@NotNull
			@Min(value = 0L, message = WRONG_SEAT_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicSeatRequestDto dto
	) {
		try {
			seatService.updateSeat(id, dto);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/seats/{id}")
	public void removeSeat(
			@NotNull
			@Min(value = 0L, message = WRONG_SEAT_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			seatService.removeSeat(id);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

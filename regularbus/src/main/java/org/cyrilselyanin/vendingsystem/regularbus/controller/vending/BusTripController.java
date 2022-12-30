package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.BasicBusTripRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.BusTripService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class BusTripController {

	private static final String WRONG_BUSTRIP_ID_ERR_MESSAGE = "Недопустимый id маршрута.";
	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";

	private final BusTripService busTripService;

	@PostMapping("/busTrips/create")
	public ResponseEntity<?> createBusTrip(
			@RequestBody @Valid BasicBusTripRequestDto dto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/busTrips/create")
							.toUriString()
			);
			busTripService.createBusTrip(dto);
			return ResponseEntity
					.created(uri)
					.build();
		} catch (RuntimeException ex) {
			log.error("There is a create bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/busTrips/{id}")
	public GetBusTripResponseDto getBusTrip(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			return busTripService.getBusTrip(id);
		} catch (RuntimeException ex) {
			log.error("There is a get bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/busTrips/carrier/{carrierId}")
	public Page<GetBusTripResponseDto> getBusTrips(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long carrierId,
			Pageable pageable
	) {
		try {
			return busTripService.getBusTripsByCarrierId(carrierId, pageable);
		} catch (RuntimeException ex) {
			log.error("There is a get bus list error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/busTrips/{id}")
	public void updateBusTrip(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicBusTripRequestDto dto
	) {
		try {
			busTripService.updateBusTrip(id, dto);
		} catch (RuntimeException ex) {
			log.error("There is an update bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/busTrips/{id}")
	public void removeBusTrip(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			busTripService.removeBusTrip(id);
		} catch (RuntimeException ex) {
			log.error("There is a remove bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/busTrips/search")
	public Page<GetBusTripResponseDto> searchBusTrips(
			@NotBlank(message = "Название пункта отправления не указано")
			@RequestParam("arrivalPointTo")
			String arrivalBusPointName,
			@NotBlank(message = "Дата отправления не указана")
			@RequestParam("departureDate")
			String departureDateString,
			Pageable pageable
	) {
		try {
			return busTripService
					.getBusTripsByArrivalAndDateTime(
							arrivalBusPointName, departureDateString, pageable
					);
		} catch (RuntimeException ex) {
			log.error("There is a get bustrips by arrival point and date error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.BasicBusRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.BusService;
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
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class BusController {

	private static final String WRONG_BUS_ID_ERR_MESSAGE = "Недопустимый id автобуса.";
	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";

	private final BusService busService;

	@PostMapping("/buses/create")
	public ResponseEntity<GetBusResponseDto> createBus(
			@RequestBody @Valid BasicBusRequestDto dto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/buses/create")
							.toUriString()
			);
			return ResponseEntity
					.created(uri)
					.body(busService.createBus(dto));
		} catch (RuntimeException ex) {
			log.error("There is a create bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/buses/{id}")
	public GetBusResponseDto getBus(
			@NotNull
			@Min(value = 0L, message = WRONG_BUS_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			return busService.getBus(id);
		} catch (RuntimeException ex) {
			log.error("There is a get bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/buses/carrier/{carrierId}")
	public Page<GetBusResponseDto> getBuses(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long carrierId,
			Pageable pageable
	) {
		try {
			return busService.getBusesByCarrierId(carrierId, pageable);
		} catch (RuntimeException ex) {
			log.error("There is a get buses error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/buses/{id}")
	public GetBusResponseDto updateBus(
			@NotNull
			@Min(value = 0L, message = WRONG_BUS_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicBusRequestDto dto
	) {
		try {
			return busService.updateBus(id, dto);
		} catch (RuntimeException ex) {
			log.error("There is an update bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/buses/{id}")
	public void removeBus(
			@NotNull
			@Min(value = 0L, message = WRONG_BUS_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			busService.removeBus(id);
		} catch (RuntimeException ex) {
			log.error("There is a remove bus error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.BasicBusPointRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.GetBusPointResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.BusPointService;
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
public class BusPointController {

	private static final String WRONG_BUSPOINT_ID_ERR_MESSAGE = "Недопустимый id пункта остановки.";

	private final BusPointService busPointService;

	@PostMapping("/busPoints/create")
	public ResponseEntity<GetBusPointResponseDto> createBusPoint(
			@RequestBody @Valid BasicBusPointRequestDto dto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/busPoints/create")
							.toUriString()
			);
			return ResponseEntity
					.created(uri)
					.body(busPointService.createBusPoint(dto));
		} catch (RuntimeException ex) {
			log.error("There is a create bus point error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/busPoints/{id}")
	public GetBusPointResponseDto getBusPoint(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSPOINT_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			return busPointService.getBusPoint(id);
		} catch (RuntimeException ex) {
			log.error("There is a get bus point error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/busPoints")
	public Page<GetBusPointResponseDto> getBusPoints(Pageable pageable) {
		try {
			return busPointService.getBusPoints(pageable);
		} catch (RuntimeException ex) {
			log.error("There is a get bus points error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/busPoints/{id}")
	public GetBusPointResponseDto updateBusPoint(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSPOINT_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicBusPointRequestDto dto
	) {
		try {
			return busPointService.updateBusPoint(id, dto);
		} catch (RuntimeException ex) {
			log.error("There is an update bus point error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/busPoints/{id}")
	public void removeBusPoint(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSPOINT_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			busPointService.removeBusPoint(id);
		} catch (RuntimeException ex) {
			log.error("There is a remove bus point error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

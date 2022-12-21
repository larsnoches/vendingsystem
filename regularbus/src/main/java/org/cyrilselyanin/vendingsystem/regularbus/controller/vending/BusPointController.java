package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.BasicBusPointRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.GetBusPointResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.BusPointService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class BusPointController {

	private static final String WRONG_BUSPOINT_ID_ERR_MESSAGE = "Недопустимый id пункта остановки.";

	private final BusPointService busPointService;

	@PostMapping("/busPoints/create")
	public ResponseEntity<GetBusPointResponseDto> createBusPoint(
			@RequestBody @Valid BasicBusPointRequestDto dto
	) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/api/v1/busPoints/create")
						.toUriString()
		);
		return ResponseEntity
				.created(uri)
				.body(busPointService.createBusPoint(dto));
	}

	@GetMapping("/busPoints/{id}")
	public GetBusPointResponseDto getBusPoint(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSPOINT_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		return busPointService.getBusPoint(id);
	}

	@GetMapping("/busPoints")
	public Page<GetBusPointResponseDto> getBusPoints(Pageable pageable) {
		return busPointService.getBusPoints(pageable);
	}

	@PutMapping("/busPoints/{id}")
	public GetBusPointResponseDto updateBusPoint(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSPOINT_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicBusPointRequestDto dto
	) {
		return busPointService.updateBusPoint(id, dto);
	}

	@DeleteMapping("/busPoints/{id}")
	public void removeBusPoint(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSPOINT_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		busPointService.removeBusPoint(id);
	}

}

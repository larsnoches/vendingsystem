package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.BasicBusRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.BusService;
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
public class BusController {

	private static final String WRONG_BUS_ID_ERR_MESSAGE = "Недопустимый id автобуса.";
	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";

	private final BusService busService;

	@PostMapping("/buses/create")
	public ResponseEntity<GetBusResponseDto> createBus(
			@RequestBody @Valid BasicBusRequestDto dto
	) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/api/v1/buses/create")
						.toUriString()
		);
		return ResponseEntity
				.created(uri)
				.body(busService.createBus(dto));
	}

	@GetMapping("/buses/{id}")
	public GetBusResponseDto getBus(
			@NotNull
			@Min(value = 0L, message = WRONG_BUS_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		return busService.getBus(id);
	}

	@GetMapping("/buses/carrier/{carrierId}")
	public Page<GetBusResponseDto> getBuses(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long carrierId,
			Pageable pageable
	) {
		return busService.getBusesByCarrierId(carrierId, pageable);
	}

	@PutMapping("/buses/{id}")
	public GetBusResponseDto updateBus(
			@NotNull
			@Min(value = 0L, message = WRONG_BUS_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicBusRequestDto dto
	) {
		return busService.updateBus(id, dto);
	}

	@DeleteMapping("/buses/{id}")
	public void removeBus(
			@NotNull
			@Min(value = 0L, message = WRONG_BUS_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		busService.removeBus(id);
	}

}

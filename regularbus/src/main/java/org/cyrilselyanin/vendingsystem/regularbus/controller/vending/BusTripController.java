package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.BasicBusTripRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.BusTripService;
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
public class BusTripController {

	private static final String WRONG_BUSTRIP_ID_ERR_MESSAGE = "Недопустимый id маршрута.";
	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";

	private final BusTripService busTripService;

	@PostMapping("/busTrips/create")
	public ResponseEntity<GetBusTripResponseDto> createBusTrip(
			@RequestBody @Valid BasicBusTripRequestDto dto
	) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/api/v1/busTrips/create")
						.toUriString()
		);
		return ResponseEntity
				.created(uri)
				.body(busTripService.createBusTrip(dto));
	}

	@GetMapping("/busTrips/{id}")
	public GetBusTripResponseDto getBusTrip(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		return busTripService.getBusTrip(id);
	}

	@GetMapping("/busTrips/carrier/{carrierId}")
	public Page<GetBusTripResponseDto> getBusTrips(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long carrierId,
			Pageable pageable
	) {
		return busTripService.getBusTripsByCarrierId(carrierId, pageable);
	}

	@PutMapping("/busTrips/{id}")
	public GetBusTripResponseDto updateBusTrip(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicBusTripRequestDto dto
	) {
		return busTripService.updateBusTrip(id, dto);
	}

	@DeleteMapping("/busTrips/{id}")
	public void removeBusTrip(
			@NotNull
			@Min(value = 0L, message = WRONG_BUSTRIP_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		busTripService.removeBusTrip(id);
	}

}

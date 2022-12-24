package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.BasicFareRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.GetFareResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.FareService;
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
public class FareController {

	private static final String WRONG_FARE_ID_ERR_MESSAGE = "Недопустимый id тарифа.";
	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";

	private final FareService fareService;

	@PostMapping("/fares/create")
	public ResponseEntity<?> createFare(
			@RequestBody @Valid BasicFareRequestDto dto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/fares/create")
							.toUriString()
			);
			fareService.createFare(dto);
			return ResponseEntity
					.created(uri)
					.build();
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/fares/{id}")
	public GetFareResponseDto getFare(
			@NotNull
			@Min(value = 0L, message = WRONG_FARE_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			return fareService.getFare(id);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/fares/carrier/{carrierId}")
	public Page<GetFareResponseDto> getFares(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long carrierId,
			Pageable pageable
	) {
		try {
			return fareService.getFareesByCarrierId(carrierId, pageable);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/fares/{id}")
	public void updateFare(
			@NotNull
			@Min(value = 0L, message = WRONG_FARE_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicFareRequestDto dto
	) {
		try {
			fareService.updateFare(id, dto);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/fares/{id}")
	public void removeFare(
			@NotNull
			@Min(value = 0L, message = WRONG_FARE_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			fareService.removeFare(id);
		} catch (RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

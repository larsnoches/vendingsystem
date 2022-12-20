package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.BasicCarrierRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.CarrierService;
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
public class CarrierController {

	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";

	private final CarrierService carrierService;

	@PostMapping("/carriers/create")
	public ResponseEntity<GetCarrierResponseDto> createCarrier(
			@RequestBody @Valid BasicCarrierRequestDto dto
	) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/api/v1/carriers/create")
						.toUriString()
		);
		return ResponseEntity
				.created(uri)
				.body(carrierService.createCarrier(dto));
	}

	@GetMapping("/carriers/{id}")
	public GetCarrierResponseDto getCarrier(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		return carrierService.getCarrier(id);
	}

	@GetMapping("/carriers")
	public Page<GetCarrierResponseDto> getCarriers(Pageable pageable) {
		return carrierService.getCarriers(pageable);
	}

	@PutMapping("/carriers/{id}")
	public GetCarrierResponseDto updateCarrier(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicCarrierRequestDto dto
	) {
		return carrierService.updateCarrier(id, dto);
	}

	@DeleteMapping("/carriers/{id}")
	public void removeCarrier(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		carrierService.removeCarrier(id);
	}

}

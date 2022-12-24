package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.BasicCarrierRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.CarrierService;
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
public class CarrierController {

	private static final String WRONG_CARRIER_ID_ERR_MESSAGE = "Недопустимый id поставщика.";

	private final CarrierService carrierService;

	@PostMapping("/carriers/create")
	public ResponseEntity<?> createCarrier(
			@RequestBody @Valid BasicCarrierRequestDto dto
	) {
		try {
			URI uri = URI.create(
					ServletUriComponentsBuilder.fromCurrentContextPath()
							.path("/api/v1/carriers/create")
							.toUriString()
			);
			carrierService.createCarrier(dto);
			return ResponseEntity
					.created(uri)
					.build();
		} catch (RuntimeException ex) {
			log.error("There is a create carrier error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/carriers/{id}")
	public GetCarrierResponseDto getCarrier(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			return carrierService.getCarrier(id);
		} catch (RuntimeException ex) {
			log.error("There is a get carrier error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping("/carriers")
	public Page<GetCarrierResponseDto> getCarriers(Pageable pageable) {
		try {
			return carrierService.getCarriers(pageable);
		} catch (RuntimeException ex) {
			log.error("There is a get carriers error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping("/carriers/{id}")
	public void updateCarrier(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long id,
			@RequestBody @Valid BasicCarrierRequestDto dto
	) {
		try {
			carrierService.updateCarrier(id, dto);
		} catch (RuntimeException ex) {
			log.error("There is a update carrier error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/carriers/{id}")
	public void removeCarrier(
			@NotNull
			@Min(value = 0L, message = WRONG_CARRIER_ID_ERR_MESSAGE)
			@PathVariable Long id
	) {
		try {
			carrierService.removeCarrier(id);
		} catch (RuntimeException ex) {
			log.error("There is a remove carrier error.", ex.getCause());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

}

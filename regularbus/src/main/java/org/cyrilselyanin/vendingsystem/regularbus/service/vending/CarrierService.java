package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.BasicCarrierRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CarrierService {

	@PreAuthorize("hasRole('MANAGER')")
	void createCarrier(BasicCarrierRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	GetCarrierResponseDto getCarrier(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetCarrierResponseDto> getCarriers(Pageable pageable);

	@PreAuthorize("hasRole('MANAGER')")
	void updateCarrier(Long id, BasicCarrierRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	void removeCarrier(Long id);

}

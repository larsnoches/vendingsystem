package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.BasicFareRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.GetFareResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface FareService {

	@PreAuthorize("hasRole('MANAGER')")
	GetFareResponseDto createFare(BasicFareRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	GetFareResponseDto getFare(Long id);

	@PreAuthorize("hasRole('MANAGER')")
	Page<GetFareResponseDto> getFareesByCarrierId(Long carrierId, Pageable pageable);

	@PreAuthorize("hasRole('MANAGER')")
	GetFareResponseDto updateFare(Long id, BasicFareRequestDto dto);

	@PreAuthorize("hasRole('MANAGER')")
	void removeFare(Long id);

}
